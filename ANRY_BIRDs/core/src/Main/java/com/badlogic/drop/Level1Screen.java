package com.badlogic.drop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Level1Screen implements Screen {
    private final AngryBird game;

    // Constants
    private static final float WORLD_WIDTH = 12f; // World width in meters
    private static final float WORLD_HEIGHT = 8f; // World height in meters
    private static final float TIMESTEP = 1 / 60f;
    private static final int VELOCITY_ITERATIONS = 8;
    private static final int POSITION_ITERATIONS = 3;

    private OrthographicCamera camera;
    private Viewport viewport;
    private SpriteBatch batch;
    private Box2DDebugRenderer debugRenderer;
    private ShapeRenderer shapeRenderer;

    private Sprite background, slingshotSprite, groundSprite;
    private ArrayList<Bird> birds;
    private ArrayList<Block> blocks;
    private ArrayList<Pig> pigs;

    private World world;
    private Music bgm;

    // Bird selection and trajectory
    private Bird selectedBird;
    private float launchPower;
    private float launchAngle;


    public Level1Screen(AngryBird game) {
        this.game = game;
        initialize();
    }

    private void initialize() {
        // Camera and viewport setup
        camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera); // Maintain aspect ratio
        viewport.apply();
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();

        batch = new SpriteBatch();
        debugRenderer = new Box2DDebugRenderer();
        shapeRenderer = new ShapeRenderer();
        world = new World(new Vector2(0, -9.8f), true); // Gravity setup

        // Add a contact listener for collision detection
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                handleCollision(contact);
            }

            @Override
            public void endContact(Contact contact) {}

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {}

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {}
        });


        // Initialize assets
        initializeBackground();
        initializeGround();
        initializeSlingshot();
        initializeBirds();
        initializeBlocks();
        initializePigs();
        initializeBoundaries();


        // Background music
        bgm = Gdx.audio.newMusic(Gdx.files.internal("angry_birds.mp3"));
        bgm.setLooping(true);
        bgm.setVolume(0.1f);
        bgm.play();

    }
    private void initializeBoundaries() {
        // Define the boundary body
        BodyDef boundaryBodyDef = new BodyDef();
        boundaryBodyDef.type = BodyDef.BodyType.StaticBody;

        // Left boundary
        boundaryBodyDef.position.set(0, WORLD_HEIGHT / 2); // Left edge
        Body leftBoundary = world.createBody(boundaryBodyDef);

        PolygonShape leftShape = new PolygonShape();
        leftShape.setAsBox(0.1f, WORLD_HEIGHT / 2); // Thin vertical boundary
        leftBoundary.createFixture(leftShape, 0);
        leftShape.dispose();

        // Right boundary
        boundaryBodyDef.position.set(WORLD_WIDTH, WORLD_HEIGHT / 2); // Right edge
        Body rightBoundary = world.createBody(boundaryBodyDef);

        PolygonShape rightShape = new PolygonShape();
        rightShape.setAsBox(0.1f, WORLD_HEIGHT / 2); // Thin vertical boundary
        rightBoundary.createFixture(rightShape, 0);
        rightShape.dispose();

        // Top boundary
        boundaryBodyDef.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT); // Top edge
        Body topBoundary = world.createBody(boundaryBodyDef);

        PolygonShape topShape = new PolygonShape();
        topShape.setAsBox(WORLD_WIDTH / 2, 0.1f); // Thin horizontal boundary
        topBoundary.createFixture(topShape, 0);
        topShape.dispose();
    }

    private void handleCollision(Contact contact) {
        Object userDataA = contact.getFixtureA().getBody().getUserData();
        Object userDataB = contact.getFixtureB().getBody().getUserData();

        // Bird-Pig collision
        if (userDataA instanceof Pig && userDataB instanceof Bird) {
            ((Pig) userDataA).takeDamage(10);
            ((Bird) userDataB).setCollided();
        } else if (userDataA instanceof Bird && userDataB instanceof Pig) {
            ((Pig) userDataB).takeDamage(10);
            ((Bird) userDataA).setCollided();
        }

        // Bird-Block collision
        if (userDataA instanceof Block && userDataB instanceof Bird) {
            ((Block) userDataA).takeDamage(10);
            ((Bird) userDataB).setCollided();
        } else if (userDataA instanceof Bird && userDataB instanceof Block) {
            ((Block) userDataB).takeDamage(10);
            ((Bird) userDataA).setCollided();
        }

        // Pig-Block collision
        if (userDataA instanceof Block && userDataB instanceof Pig) {
            Pig pig = (Pig) userDataB;
            float impactForce = contact.getFixtureA().getBody().getLinearVelocity().len();
            pig.applyBlockHitDamage(impactForce); // Damage from block collision
        } else if (userDataA instanceof Pig && userDataB instanceof Block) {
            Pig pig = (Pig) userDataA;
            float impactForce = contact.getFixtureB().getBody().getLinearVelocity().len();
            pig.applyBlockHitDamage(impactForce); // Damage from block collision
        }
    }


    private void initializeBackground() {
        background = new Sprite(new Texture("game_background.png"));
        background.setSize(WORLD_WIDTH, WORLD_HEIGHT);
        background.setPosition(0, 0);
    }

    private void initializeGround() {
        groundSprite = new Sprite(new Texture("ground.png"));
        groundSprite.setSize(12f, 0.7f); // Full width of the level
        groundSprite.setPosition(0, 0);

        // Ground physics body
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody;
        groundBodyDef.position.set(6.0f, 0.35f); // Centered horizontally
        Body groundBody = world.createBody(groundBodyDef);

        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsBox(6.0f, 0.35f); // Full width
        groundBody.createFixture(groundShape, 0);
        groundShape.dispose();
    }

    private void initializeSlingshot() {
        slingshotSprite = new Sprite(new Texture("slingshot.png"));
        slingshotSprite.setSize(0.8f, 1.6f); // Adjusted size
        slingshotSprite.setPosition(1.0f, 0.5f); // Position near bottom-left
    }

    private void initializeBirds() {
        birds = new ArrayList<>();
        birds.add(new Bird("bird1.png", 1.2f, 0.6f, world)); // Bird near slingshot
        birds.add(new Bird("bird2.png", 1.4f, 0.6f, world)); // Bird near slingshot
        birds.add(new Bird("bird3.png", 1.6f, 0.6f, world)); // Bird on slingshot

        for (Bird bird : birds) {
            bird.getBody().setUserData(bird); // Set user data for collision detection
            bird.getBirdSprite().setSize(0.5f, 0.5f); // Proper scaling
        }
    }

    private void initializeBlocks() {
    blocks = new ArrayList<>();

    // Vertical blocks
    blocks.add(new Block(new Texture("wood_block.png"), 7.5f, 1.0f, 0.2f, 1.7f, world, 10)); // Left vertical block
    blocks.add(new Block(new Texture("wood_block.png"), 9.0f, 1.0f, 0.2f, 1.7f, world, 10)); // Right vertical block
    blocks.add(new Block(new Texture("wood_block.png"), 8.25f, 1.0f, 0.2f, 1.7f, world, 10)); // Middle vertical block

    // Horizontal block on top
    float horizontalBlockWidth = 2.4f; // Width of the horizontal block
    float horizontalBlockHeight = 0.2f; // Height of the horizontal block
    float verticalBlockHeight = 1.7f; // Height of the vertical blocks

    // Adjust position so the horizontal block rests stably on top of the three vertical blocks
    float horizontalBlockY = 1.0f + verticalBlockHeight + horizontalBlockHeight / 2; // Top of vertical blocks + half height of horizontal block
    blocks.add(new Block(new Texture("wood_block.png"), 8.25f, horizontalBlockY, horizontalBlockWidth, horizontalBlockHeight, world, 10));
}

    private Block createDynamicBlock(String texturePath, float x, float y, float width, float height) {
        Texture texture = new Texture(texturePath);

        // Create the block
        Block block = new Block(texture, x, y, width, height, world,3); // Lower health (e.g., 3)

        // Define the physics body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody; // Dynamic for falling blocks
        bodyDef.position.set(x, y);

        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2); // Adjusted size for smaller blocks

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.3f;        // Lower density for easy falling
        fixtureDef.friction = 0.2f;       // Reduced friction for easier sliding
        fixtureDef.restitution = 0.5f;    // Increase bounce for better reaction

        body.createFixture(fixtureDef);
        shape.dispose();

        // Assign the body to the block
        block.setBody(body);

        return block;
    }

    // Utility method to create blocks
    private Block createBlock(String texturePath, float x, float y, float width, float height, BodyDef.BodyType bodyType) {
        Texture texture = new Texture(texturePath);
        return new Block(
            texture,
            x,
            y,
            width,
            height,
            world,
            10 // Health
        );
    }

private void initializePigs() {
    pigs = new ArrayList<>();

    // Ensure there are enough blocks before accessing the 4th block
    if (blocks.size() > 3) {
        Block horizontalBlock = blocks.get(3); // Get the 4th block (assumed to be horizontal)

        // Get the block's position and size
        float blockX = horizontalBlock.getBody().getPosition().x;
        float blockY = horizontalBlock.getBody().getPosition().y;
        float blockWidth = horizontalBlock.getBlockSprite().getWidth();
        float blockHeight = horizontalBlock.getBlockSprite().getHeight();

        // Pig dimensions
        float pigWidth = 0.4f;
        float pigHeight = 0.4f;

        // Calculate positions for three pigs distributed evenly on the horizontal block
        float pigY = blockY + blockHeight / 2 + pigHeight / 2; // Position pigs just above the block
        float pigX1 = blockX - blockWidth / 3; // Left pig
        float pigX2 = blockX; // Center pig
        float pigX3 = blockX + blockWidth / 3; // Right pig

        // Add pigs to the list
        pigs.add(new Pig("pig.png", pigX1, pigY, world, 10)); // Left pig
        pigs.add(new Pig("pig.png", pigX2, pigY, world, 10)); // Center pig
        pigs.add(new Pig("pig.png", pigX3, pigY, world, 10)); // Right pig

        // Set proper size for each pig sprite
        for (Pig pig : pigs) {
            pig.getPigSprite().setSize(pigWidth, pigHeight); // Proper scaling
        }
    } else {
        System.err.println("Not enough blocks to position pigs.");
    }
}

    private Pig createPig(String texturePath, float x, float y) {
        Pig pig = new Pig(texturePath, x, y, world, 10); // Create the pig
        pig.getBody().setLinearDamping(5.0f); // Reduce sliding
        pig.getBody().setAngularDamping(5.0f); // Reduce rotation
        return pig;
    }

    @Override
    public void render(float delta) {
        handleInput(); // Handle input for bird dragging and launching

        // Clear the screen
        Gdx.gl.glClearColor(0.6f, 0.8f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Step the physics simulation
        world.step(TIMESTEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);

        // Update camera and batch
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        // Render game objects
        batch.begin();
        drawBackground();
        drawGround();
        drawBlocks();
        drawPigs();
        drawBirds();
        drawSlingshot();
        batch.end();

        // Update game state
        updateAndRemoveBlocks();
        updatePigs(delta); // Update pig states (fall damage, removal)
        updateAndRemoveBirds(delta);

        // Draw trajectory if a bird is selected
        if (selectedBird != null) {
            drawTrajectory();
        }

        // Check win/loss conditions
        checkGameState();

        debugRenderer.render(world, camera.combined);
    }

    // Utility method to update and remove stationary birds
    private void updateAndRemoveBirds(float delta) {
        List<Bird> toRemove = new ArrayList<>();

        for (Bird bird : birds) {
            if (bird.isLaunched() && bird.isStopped()) {
                bird.setCollided(); // Mark bird as collided when it stops
            }

            // Remove bird after the delay
            if (bird.isReadyToRemove(delta)) {
                world.destroyBody(bird.getBody());
                toRemove.add(bird);
            }
        }

        birds.removeAll(toRemove); // Remove marked birds from the list
    }

    private void updateAndRemoveBlocks() {
        List<Block> toRemove = new ArrayList<>();

        for (Block block : blocks) {
            if (block.isDestroyed()) {
                // Remove the block's body from the world
                world.destroyBody(block.getBody());

                // Check for pigs above the block and despawn them
                Iterator<Pig> pigIterator = pigs.iterator();
                while (pigIterator.hasNext()) {
                    Pig pig = pigIterator.next();
                    float pigX = pig.getBody().getPosition().x;
                    float pigY = pig.getBody().getPosition().y;

                    float blockX = block.getBody().getPosition().x;
                    float blockY = block.getBody().getPosition().y;

                    // Check if the pig is above the block
                    if (Math.abs(pigX - blockX) < block.getBlockSprite().getWidth() / 2 &&
                        pigY > blockY + block.getBlockSprite().getHeight() / 2) {
                        pig.despawn(world);
                        pigIterator.remove();
                    }
                }

                toRemove.add(block);
            }
        }

        // Remove blocks from the list
        blocks.removeAll(toRemove);
    }

    private void updatePigs(float delta) {
        Iterator<Pig> pigIterator = pigs.iterator();

        while (pigIterator.hasNext()) {
            Pig pig = pigIterator.next();

            // Update pig sprite position
            pig.updateSprite();

            // Check if the pig is dead
            if (pig.isDead()) {
                pig.despawn(world); // Despawn the pig
                pigIterator.remove(); // Remove from the list
            }
        }
    }

    // Utility method to update and remove pigs
    private void updateAndRemovePigs(float delta) {
        List<Pig> toRemove = new ArrayList<>();

        for (Pig pig : pigs) {
            pig.updateSprite(); // Update pig's sprite position and rotation

            // Apply fall damage or other conditions
            pig.applyFallDamage();

            // Remove pig after the delay if health is 0 or below
            if (pig.isReadyToRemove(delta)) {
                world.destroyBody(pig.getBody());
                toRemove.add(pig);
            }
        }

        pigs.removeAll(toRemove); // Remove marked pigs from the list
    }

    private void checkGameState() {
        if (pigs.isEmpty()) {
            // All pigs are eliminated, level is complete
            game.setScreen(new LevelCompleteScreen(game, 1)); // Transition to "Level Passed" screen
        } else if (birds.isEmpty() && selectedBird == null) {
            // No birds left and no selected bird, level failed
            game.setScreen(new LevelFailedScreen(game, 1, new Skin(Gdx.files.internal("uiskin.json")))); // Transition to "Level Failed" screen
        }
    }

    private void handleInput() {
        if (Gdx.input.isTouched()) {
            Vector3 touchPoint = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            if (selectedBird == null) {
                // Select the bird if clicked
                for (Bird bird : birds) {
                    if (!bird.isLaunched() && bird.getBoundingBox().contains(touchPoint.x, touchPoint.y)) {
                        selectedBird = bird;
                        // Automatically position the bird at a slightly higher point above the slingshot head
                        float slingshotHeadX = slingshotSprite.getX() + slingshotSprite.getWidth() / 2;
                        float slingshotHeadY = slingshotSprite.getY() + slingshotSprite.getHeight() + 0.4f; // Increased height offset
                        selectedBird.moveToSlingshot(slingshotHeadX, slingshotHeadY); // Position at slingshot head
                        break;
                    }
                }
            } else {
                // Dragging the bird within the defined range
                float slingshotHeadX = slingshotSprite.getX() + slingshotSprite.getWidth() / 2;
                float slingshotHeadY = slingshotSprite.getY() + slingshotSprite.getHeight() + 0.4f; // Match the new height

                float dx = touchPoint.x - slingshotHeadX;
                float dy = touchPoint.y - slingshotHeadY;
                float distance = Vector2.dst(slingshotHeadX, slingshotHeadY, touchPoint.x, touchPoint.y);

                if (distance <= 1.0f) { // Slightly larger drag area
                    selectedBird.updatePosition(touchPoint.x, touchPoint.y);
                } else {
                    // Limit dragging to the boundary of the allowed range
                    float angle = (float) Math.atan2(dy, dx);
                    float limitedX = slingshotHeadX + (float) Math.cos(angle) * 1.0f;
                    float limitedY = slingshotHeadY + (float) Math.sin(angle) * 1.0f;
                    selectedBird.updatePosition(limitedX, limitedY);
                }

                // Calculate reversed trajectory direction
                launchAngle = (float) Math.toDegrees(Math.atan2(slingshotHeadY - selectedBird.getBody().getPosition().y,
                    slingshotHeadX - selectedBird.getBody().getPosition().x));
                launchPower = Math.min(distance, 1.0f) * 10; // Adjust power scaling
            }
        } else if (selectedBird != null) {
            // Launch the bird when released
            Vector2 launchVelocity = calculateLaunchVelocity(launchPower, launchAngle);
            selectedBird.launch(launchVelocity);
            selectedBird.setLaunched(true);
            selectedBird = null; // Reset selected bird
        }
    }

    private float calculateAngle(float touchX, float touchY, float slingshotX, float slingshotY) {
        return (float) Math.toDegrees(Math.atan2(slingshotY - touchY, slingshotX - touchX));
    }

    private float calculatePower(float touchX, float touchY, float slingshotX, float slingshotY) {
        return Vector2.dst(touchX, touchY, slingshotX, slingshotY); // Distance as power
    }

    private Vector2 calculateLaunchVelocity(float power, float angle) {
        Vector2 velocity = new Vector2(power, 0);
        velocity.setAngleDeg(angle);
        return velocity;
    }

    private void drawTrajectory() {
        if (selectedBird == null) return;

        float slingshotX = slingshotSprite.getX() + 0.25f;
        float slingshotY = slingshotSprite.getY() + 0.75f;

        // Calculate trajectory points
        List<Vector2> trajectoryPoints = calculateTrajectory(
            slingshotX,
            slingshotY,
            launchPower,
            launchAngle,
            9.8f, // Gravity
            30,   // Number of points
            0.1f  // Time step
        );

        // Draw the trajectory
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 0, 0, 1); // Set color to red (RGBA)

        for (int i = 0; i < trajectoryPoints.size() - 1; i++) {
            Vector2 start = trajectoryPoints.get(i);
            Vector2 end = trajectoryPoints.get(i + 1);
            shapeRenderer.line(start.x, start.y, end.x, end.y); // Draw a line segment
        }

        shapeRenderer.end();
    }

    private List<Vector2> calculateTrajectory(float initialX, float initialY, float velocity, float angle, float gravity, int pointCount, float timeStep) {
        List<Vector2> trajectoryPoints = new ArrayList<>();

        float radians = (float) Math.toRadians(angle); // Convert angle to radians
        float velocityX = velocity * (float) Math.cos(radians); // Horizontal velocity
        float velocityY = velocity * (float) Math.sin(radians); // Vertical velocity

        for (int i = 0; i < pointCount; i++) {
            float t = i * timeStep; // Time step
            float x = initialX + velocityX * t; // X position
            float y = initialY + velocityY * t - 0.5f * gravity * t * t; // Y position

            // Stop trajectory if it hits the ground
            if (y < 0) break;

            trajectoryPoints.add(new Vector2(x, y));
        }

        return trajectoryPoints;
    }

    private void drawBackground() {
        background.draw(batch);
    }

    private void drawGround() {
        groundSprite.draw(batch);
    }

    private void drawSlingshot() {
        slingshotSprite.draw(batch);
    }

    private void drawBlocks() {
        for (Block block : blocks) {
            block.updateSprite();
            block.getBlockSprite().draw(batch);
        }
    }

    private void drawPigs() {
        for (Pig pig : pigs) {
            pig.draw(batch); // Use the new draw method in the Pig class
        }
    }

    private void drawBirds() {
        for (Bird bird : birds) {
            bird.updateSprite();
            bird.getBirdSprite().draw(batch);
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height,true); // Adjust viewport on resize
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        batch.dispose();
        debugRenderer.dispose();
        world.dispose();
        bgm.dispose();
        background.getTexture().dispose();
        groundSprite.getTexture().dispose();
        slingshotSprite.getTexture().dispose();
        for (Bird bird : birds) bird.dispose();
        for (Block block : blocks) block.dispose();
        for (Pig pig : pigs) pig.dispose();
    }

    @Override
    public void show() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide(){}
}
