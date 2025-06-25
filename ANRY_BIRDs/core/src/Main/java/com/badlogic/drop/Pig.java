package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;

public class Pig {
    private Texture texture;
    private Body body;
    private Sprite sprite;
    private boolean isHit = false;
    private boolean isRemoved = false; // New flag to track removal
    private int health; // Health of the pig
    private float timer = 0f; // Timer to track time after being hit
    private float lastVelocityY = 0f; // To calculate fall damage

    // Constructor to create a pig in the game world
    public Pig(String texturePath, float x, float y, World world, int initialHealth) {
//        this.pigTexture = new Texture(texturePath);
        this.health = initialHealth; // Set initial health

        // Create sprite
        this.texture = new Texture(Gdx.files.internal(texturePath));
        this.sprite = new Sprite(texture);
        this.sprite.setSize(0.4f, 0.4f); // Adjust size as needed
        this.sprite.setOriginCenter();

        // Create Box2D body for the pig
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);

        this.body = world.createBody(bodyDef);

        // Define the pig's shape and properties
        CircleShape shape = new CircleShape();
        shape.setRadius(0.2f); // Adjust radius to match sprite size

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0f; // Some bounce

        this.body.createFixture(fixtureDef);
        shape.dispose();
        this.body.setUserData(this);
    }

    // Update sprite position and rotation to match the physics body
    public void updateSprite() {
        if (body != null && !isRemoved) {
            sprite.setPosition(
                body.getPosition().x - sprite.getWidth() / 2,
                body.getPosition().y - sprite.getHeight() / 2
            );
        }
    }

    public void draw(SpriteBatch batch) {
        if (!isRemoved) {
            sprite.draw(batch); // Only draw if not removed
        }
    }

    public Sprite getSprite() {
        return sprite;
    }

    // Get the sprite for rendering
    public Sprite getPigSprite() {
        return sprite;
    }

    // Get the Box2D body of the pig
    public Body getBody() {
        return body;
    }

    public void takeDamage(int damage) {
        if (!isHit) {
            health -= damage;
            if (health <= 0) {
                setHit(true); // Mark as hit if health is 0 or below
            }
        }
    }

    public void applyFallDamage() {
        float currentVelocityY = Math.abs(body.getLinearVelocity().y);
        if (currentVelocityY - lastVelocityY > 2.0f) { // Threshold for fall damage
            takeDamage((int) ((currentVelocityY - lastVelocityY) * 10)); // Scale damage
        }
        lastVelocityY = currentVelocityY; // Update for next frame
    }

    public void applyBlockHitDamage(float impactForce) {
        if (impactForce > 2.0f) { // Threshold for block impact
            takeDamage((int) (impactForce * 5)); // Scale damage based on impact force
        }
    }

    // Set the pig as hit
    public void setHit(boolean hit) {
        this.isHit = hit;
    }

    // Check if the pig is hit
    public boolean isHit() {
        return isHit;
    }

    // Check if the pig is ready to be removed
    public boolean isReadyToRemove(float delta) {
        if (health <= 0) {
            timer += delta; // Start timer after health is zero
            return timer >= 1.0f; // Remove pig after 1 second
        }
        return false;
    }

    // Dispose of the pig's texture to free resources
    public void dispose() {
        texture.dispose();
    }

    public boolean isDead() {
        return health <= 0; // Dead if health is zero or below
    }

    public boolean isRemoved() {
        return isRemoved; // Removed from the world
    }

    public void despawn(World world) {
        if (body != null) {
            world.destroyBody(body); // Remove physics body
            body = null;
        }
        sprite = null; // Remove sprite
        isRemoved = true; // Mark as removed
    }

}
