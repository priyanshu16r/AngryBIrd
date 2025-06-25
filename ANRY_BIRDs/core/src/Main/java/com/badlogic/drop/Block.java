package com.badlogic.drop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Block {
    private Texture blockTexture;
    private Body body;
    private Sprite sprite;
    private int health;
    private boolean destroyed;
    private float destructionTimer = 0f; // Timer for removing the block after destruction

    public Block(Texture texture, float x, float y, float width, float height,World world, int health) {
        this.blockTexture = texture;
        this.health = health; // Set low initial health
        sprite = new Sprite(texture);
        sprite.setSize(width, height);
        sprite.setPosition(x - width / 2, y - height / 2); // Center the sprite

        // Create Box2D body for the block
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        this.body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2); // Adjust for center origin

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.8f;
        fixtureDef.restitution = 0.0f;

        body.createFixture(fixtureDef).setUserData(this); // Set user data for collision detection
        shape.dispose();

        // Create the sprite
        sprite = new Sprite(blockTexture);
        sprite.setSize(width, height);
        sprite.setOriginCenter();
    }

    public void updateSprite() {
        if (body != null) {
            sprite.setPosition(
                body.getPosition().x - sprite.getWidth() / 2,
                body.getPosition().y - sprite.getHeight() / 2
            );
            sprite.setRotation((float) Math.toDegrees(body.getAngle()));
        }
    }


    public Sprite getBlockSprite() {
        return sprite;
    }

    public Body getBody() {
        return body;
    }

    public void takeDamage(int damage) {
        if (!destroyed) {
            health -= damage;
            if (health <= 0) {
                destroyed = true; // Mark block as destroyed
            }
        }
    }

    // Method to set the Box2D body for this block
    public void setBody(Body body) {
        this.body = body;
        this.body.setUserData(this); // Set this block as user data for collision handling
    }

    // Adjust health reduction logic
    public void applyImpact(float impactForce) {
        if (!destroyed) {
            int damage = (int) impactForce * 5; // Scale damage based on force
            takeDamage(damage);
        }
    }


    public boolean isDestroyed() {
        return destroyed;
    }

    public boolean isReadyToRemove(float delta) {
        // Increment the destruction timer when the block is marked as destroyed
        if (destroyed) {
            destructionTimer += delta;
            return destructionTimer >= 1.0f; // Remove the block after 1 second
        }
        return false;
    }

    public void dispose() {
        blockTexture.dispose();
    }
}



//package com.badlogic.drop;
//
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.math.Rectangle;
//
//public class Block {
//    private Texture blockTexture;
//    private Rectangle bounds;
//
//    public Block(Texture blockTexture, float x, float y, PhysicsHelper physicsHelper) {
//        this.blockTexture = blockTexture;
//        this.bounds = new Rectangle(x, y, blockTexture.getWidth(), blockTexture.getHeight());
//    }
//
//    public Texture getBlockTexture() {
//        return blockTexture;
//    }
//
//    public Rectangle getBounds() {
//        return bounds;
//    }
//
//    public void setPosition(float x, float y) {
//        bounds.setPosition(x, y);
//    }
//
//    public void dispose() {
//        blockTexture.dispose();
//    }
//}

//package com.badlogic.drop;
//
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.Sprite;
//import com.badlogic.gdx.physics.box2d.Body;
//import com.badlogic.gdx.physics.box2d.BodyDef;
//import com.badlogic.gdx.physics.box2d.FixtureDef;
//import com.badlogic.gdx.physics.box2d.PolygonShape;
//import com.badlogic.gdx.physics.box2d.World;
//
//public class Block {
//    private Texture blockTexture;
//    private Body body;
//    private Sprite sprite;
//    private int health;
//    private boolean destroyed;
//    private float destructionTimer = 0f; // Timer for removing the block after destruction
//
//    public Block(Texture texture, float x, float y, float width, float height, World world, int initialHealth) {
//        this.blockTexture = texture;
//        this.health = initialHealth;
//        this.destroyed = false;
//
//        // Create Box2D body for the block
//        BodyDef bodyDef = new BodyDef();
//        bodyDef.type = BodyDef.BodyType.DynamicBody;
//        bodyDef.position.set(x, y);
//        this.body = world.createBody(bodyDef);
//
//        PolygonShape shape = new PolygonShape();
//        shape.setAsBox(width / 2, height / 2); // Adjust for center origin
//
//        FixtureDef fixtureDef = new FixtureDef();
//        fixtureDef.shape = shape;
//        fixtureDef.density = 1f;
//        fixtureDef.friction = 0.5f;
//        fixtureDef.restitution = 0.3f;
//
//        body.createFixture(fixtureDef).setUserData(this); // Set user data for collision detection
//        shape.dispose();
//
//        // Create the sprite
//        sprite = new Sprite(blockTexture);
//        sprite.setSize(width, height);
//        sprite.setOriginCenter();
//    }
//
//    public void updateSprite() {
//        // Synchronize sprite with Box2D body
//        sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2, body.getPosition().y - sprite.getHeight() / 2);
//        sprite.setRotation((float) Math.toDegrees(body.getAngle()));
//    }
//
//    public Sprite getBlockSprite() {
//        return sprite;
//    }
//
//    public Body getBody() {
//        return body;
//    }
//
//    public void takeDamage(int damage) {
//        if (!destroyed) {
//            health -= damage;
//            if (health <= 0) {
//                destroyed = true;
//                body.setType(BodyDef.BodyType.StaticBody); // Stop the block from moving
//            }
//        }
//    }
//
//    public boolean isDestroyed() {
//        return destroyed;
//    }
//
//    public boolean isReadyToRemove(float delta) {
//        // Increment the destruction timer when the block is marked as destroyed
//        if (destroyed) {
//            destructionTimer += delta;
//            return destructionTimer >= 1.0f; // Remove the block after 1 second
//        }
//        return false;
//    }
//
//    public void dispose() {
//        blockTexture.dispose();
//    }
//}



//package com.badlogic.drop;
//
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.math.Rectangle;
//
//public class Block {
//    private Texture blockTexture;
//    private Rectangle bounds;
//
//    public Block(Texture blockTexture, float x, float y, PhysicsHelper physicsHelper) {
//        this.blockTexture = blockTexture;
//        this.bounds = new Rectangle(x, y, blockTexture.getWidth(), blockTexture.getHeight());
//    }
//
//    public Texture getBlockTexture() {
//        return blockTexture;
//    }
//
//    public Rectangle getBounds() {
//        return bounds;
//    }
//
//    public void setPosition(float x, float y) {
//        bounds.setPosition(x, y);
//    }
//
//    public void dispose() {
//        blockTexture.dispose();
//    }
//}
