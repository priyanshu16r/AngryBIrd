package com.badlogic.drop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Bird {
    private Texture birdTexture;
    private Body body;
    private Sprite sprite;
    private boolean isLaunched = false;
    private boolean isStopped = false;
    private boolean isCollided = false;
    private float collisionTimer = 0f; // Timer for removal delay
    private static final float REMOVAL_DELAY = 2.0f; // Delay in seconds after stopping

    public Bird(String texturePath, float x, float y, World world) {
        this.birdTexture = new Texture(texturePath);

        // Create the Box2D body for the bird
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        this.body = world.createBody(bodyDef);

        // Define the bird's shape and properties
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.15f, 0.15f); // Adjust dimensions as needed

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.3f; // Less bounciness for realistic physics

        body.createFixture(fixtureDef);
        body.setUserData(this);
        shape.dispose();

        // Create the sprite for rendering
        this.sprite = new Sprite(birdTexture);
        this.sprite.setSize(0.3f, 0.3f); // Match size with physics body
    }

    public boolean isLaunched() {
        return isLaunched;
    }

    public void setLaunched(boolean launched) {
        this.isLaunched = launched;
    }

    public Sprite getBirdSprite() {
        return sprite;
    }

    public Body getBody() {
        return body;
    }

    public void updateSprite() {
        // Synchronize the sprite's position and rotation with the physics body
        sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2, body.getPosition().y - sprite.getHeight() / 2);
        sprite.setRotation((float) Math.toDegrees(body.getAngle()));
    }

    public void dispose() {
        birdTexture.dispose();
    }

    public Rectangle getBoundingBox() {
        return new Rectangle(
            sprite.getX(),
            sprite.getY(),
            sprite.getWidth(),
            sprite.getHeight()
        );
    }

    public void moveToSlingshot(float x, float y) {
        body.setTransform(x, y, body.getAngle()); // Move to the new position
        sprite.setPosition(x - sprite.getWidth() / 2, y - sprite.getHeight() / 2); // Align sprite center with slingshot head
        body.setType(BodyDef.BodyType.KinematicBody); // Temporarily disable physics
    }

    public void updatePosition(float x, float y) {
        body.setTransform(x, y, 0); // Update position directly
        sprite.setPosition(x - sprite.getWidth() / 2, y - sprite.getHeight() / 2); // Ensure sprite sync
    }

    public void launch(Vector2 velocity) {
        body.setType(BodyDef.BodyType.DynamicBody); // Re-enable physics
        body.setLinearVelocity(velocity); // Set the launch velocity
        isLaunched = true;
    }

    public void setCollided() {
        isCollided = true;
    }

    public boolean isStopped() {
        // Bird is considered stopped if velocity and angular velocity are low
        return body.getLinearVelocity().len() < 0.1f && body.getAngularVelocity() < 0.1f && isLaunched;
    }

    public boolean isReadyToRemove(float delta) {
        if (isCollided || isStopped()) {
            collisionTimer += delta; // Start the timer after the bird stops or collides
            return collisionTimer >= REMOVAL_DELAY; // Remove after the delay
        }
        return false;
    }

    public void update(float delta) {
        if (isLaunched) {
            // Check if the bird is stopped and update the timer
            if (isStopped()) {
                collisionTimer += delta;
            }
        }

        // Update the sprite position and rotation to match the physics body
        updateSprite();
    }

}
