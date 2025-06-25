package com.badlogic.drop;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class PhysicsHelper {
    public static final float PIXELS_TO_METERS = 1 / 100f; // Conversion factor for pixels to meters
    private static final float GRAVITY_X = 0f; // No horizontal gravity
    private static final float GRAVITY_Y = -10f; // Vertical gravity

    private World world;

    // Constructor initializes the world with gravity
    public PhysicsHelper() {
        this.world = new World(new Vector2(GRAVITY_X, GRAVITY_Y), true);
    }

    public World getWorld() {
        return world;
    }

    public Body createDynamicBody(float x, float y, float width, float height, float density, float friction, float restitution) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x * PIXELS_TO_METERS, y * PIXELS_TO_METERS);

        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width * PIXELS_TO_METERS / 2, height * PIXELS_TO_METERS / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;

        body.createFixture(fixtureDef);
        shape.dispose();
        return body;
    }

    public Body createStaticBody(float x, float y, float width, float height) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x * PIXELS_TO_METERS, y * PIXELS_TO_METERS);

        Body body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width * PIXELS_TO_METERS / 2, height * PIXELS_TO_METERS / 2);
        body.createFixture(shape, 0);
        shape.dispose();
        return body;
    }

    public void update(float delta) {
        world.step(delta, 6, 2);
    }

    public void dispose() {
        world.dispose();
    }
}





//package com.badlogic.drop;
//
//import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.physics.box2d.Body;
//import com.badlogic.gdx.physics.box2d.BodyDef;
//import com.badlogic.gdx.physics.box2d.FixtureDef;
//import com.badlogic.gdx.physics.box2d.PolygonShape;
//import com.badlogic.gdx.physics.box2d.World;
//
//public class PhysicsHelper {
//    private World world;
//    private static final float PIXELS_TO_METERS = 1 / 10f; // Conversion factor for pixels to meters
//    private static final float GRAVITY_X = 0f; // No horizontal gravity
//    private static final float GRAVITY_Y = -10f; // Vertical gravity
//
//    // Constructor initializes the world with gravity
//    public PhysicsHelper() {
//        this.world = new World(new Vector2(GRAVITY_X, GRAVITY_Y), true);
//    }
//
//    // Getter for the world
//    public World getWorld() {
//        return world;
//    }
//
//    // Method to create a dynamic body
//    public Body createDynamicBody(float x, float y, float width, float height, float density, float friction, float restitution) {
//        BodyDef bodyDef = new BodyDef();
//        bodyDef.type = BodyDef.BodyType.DynamicBody;
//        bodyDef.position.set(x * PIXELS_TO_METERS, y * PIXELS_TO_METERS);
//
//        Body body = world.createBody(bodyDef);
//
//        PolygonShape shape = new PolygonShape();
//        shape.setAsBox(width * PIXELS_TO_METERS / 2, height * PIXELS_TO_METERS / 2);
//
//        FixtureDef fixtureDef = new FixtureDef();
//        fixtureDef.shape = shape;
//        fixtureDef.density = density;
//        fixtureDef.friction = friction;
//        fixtureDef.restitution = restitution;
//
//        body.createFixture(fixtureDef);
//        shape.dispose();
//        return body;
//    }
//
//    // Method to create a static body
//    public Body createStaticBody(float x, float y, float width, float height) {
//        BodyDef bodyDef = new BodyDef();
//        bodyDef.type = BodyDef.BodyType.StaticBody;
//        bodyDef.position.set(x * PIXELS_TO_METERS, y * PIXELS_TO_METERS);
//
//        Body body = world.createBody(bodyDef);
//        PolygonShape shape = new PolygonShape();
//        shape.setAsBox(width * PIXELS_TO_METERS / 2, height * PIXELS_TO_METERS / 2);
//        body.createFixture(shape, 0);
//        shape.dispose();
//        return body;
//    }
//
//    // Update method for the physics simulation
//    public void update(float delta) {
//        world.step(delta, 6, 2); // Update the physics world
//    }
//
//    // Dispose method to clean up the world
//    public void dispose() {
//        world.dispose();
//    }
//}
