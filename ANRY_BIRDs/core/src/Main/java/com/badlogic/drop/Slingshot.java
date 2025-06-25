package com.badlogic.drop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.math.Vector2;

public class Slingshot {
    private Texture slingshotTexture;
    private Rectangle bounds;
    private Body body;
    private static final float MAX_PULLBACK_DISTANCE = 2.0f; // Limit pullback to match scale

    public Slingshot(Texture slingshotTexture, float x, float y, PhysicsHelper physicsHelper) {
        this.slingshotTexture = slingshotTexture;
        this.bounds = new Rectangle(x, y, 0.8f, 1.4f); // Adjusted to match visible size
        this.body = physicsHelper.createStaticBody(x, y, 0.8f, 1.4f);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public Body getBody() {
        return body;
    }

    public Vector2 getLaunchVelocity(Vector2 slingshotPosition, Vector2 birdPosition) {
        Vector2 pullbackVector = slingshotPosition.sub(birdPosition);
        float pullbackDistance = pullbackVector.len();

        if (pullbackDistance > MAX_PULLBACK_DISTANCE) {
            pullbackVector.nor().scl(MAX_PULLBACK_DISTANCE);
        }
        return pullbackVector.scl(-10);
    }

    public void dispose() {
        slingshotTexture.dispose();
    }
}











//package com.badlogic.drop;
//
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.math.Rectangle;
//import com.badlogic.gdx.physics.box2d.Body;
//import com.badlogic.gdx.math.Vector2;
//
//public class Slingshot {
//    private Texture slingshotTexture;
//    private Rectangle bounds;
//    private Body body;
//    private static final float MAX_PULLBACK_DISTANCE = 5.0f; // Maximum pullback distance in meters
//
//    public Slingshot(Texture slingshotTexture, float x, float y, PhysicsHelper physicsHelper) {
//        this.slingshotTexture = slingshotTexture;
//        this.bounds = new Rectangle(x, y, slingshotTexture.getWidth(), slingshotTexture.getHeight());
//        this.body = physicsHelper.createStaticBody(x, y, slingshotTexture.getWidth(), slingshotTexture.getHeight());
//    }
//
//    public Texture getSlingshotTexture() {
//        return slingshotTexture;
//    }
//
//    public Rectangle getBounds() {
//        return bounds;
//    }
//
//    public Body getBody() {
//        return body;
//    }
//
//    public void dispose() {
//        slingshotTexture.dispose();
//    }
//
//    // Calculate the launch velocity based on the drag distance
//    public Vector2 getLaunchVelocity(Vector2 slingshotPosition, Vector2 birdPosition) {
//        Vector2 pullbackVector = slingshotPosition.sub(birdPosition);
//        float pullbackDistance = pullbackVector.len();
//
//        // Limit the maximum pullback distance
//        if (pullbackDistance > MAX_PULLBACK_DISTANCE) {
//            pullbackVector.nor().scl(MAX_PULLBACK_DISTANCE);
//        }
//
//        // Calculate launch velocity (negative for direction)
//        return pullbackVector.scl(-10); // Scale to control launch speed
//    }
//}
//
//

