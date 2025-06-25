package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SelectLevelScreen implements Screen {
    private final AngryBird game;
    private SpriteBatch spriteBatch;
    private Texture backgroundTexture;
    private Stage stage;
    private BitmapFont font;
    private Texture level1ButtonIcon;
    private Texture level2ButtonIcon;
    private Texture level3ButtonIcon;
    private Viewport viewport;

    public SelectLevelScreen(final AngryBird game) {
        this.game = game;

        // Initialize SpriteBatch and Viewport
        spriteBatch = new SpriteBatch();
        viewport = new StretchViewport(800, 600); // Virtual resolution for consistency
        stage = new Stage(viewport);

        // Set input processor to the stage
        Gdx.input.setInputProcessor(stage);

        // Load assets
        backgroundTexture = new Texture("Levelback.png");
        level1ButtonIcon = new Texture("level1.png");
        level2ButtonIcon = new Texture("level2.png");
        level3ButtonIcon = new Texture("level3.png");
        font = new BitmapFont(Gdx.files.internal("default.fnt"));

        // Dynamically calculate button positions based on the viewport
        createLevelButton(level1ButtonIcon, 1, 0.25f); // 25% from the left
        createLevelButton(level2ButtonIcon, 2, 0.5f);  // Centered horizontally
        createLevelButton(level3ButtonIcon, 3, 0.75f); // 75% from the left
    }


    private void createLevelButton(Texture icon, final int level, float positionX) {
        // Dynamically create button style
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = new TextureRegionDrawable(new TextureRegion(icon));
        buttonStyle.down = new TextureRegionDrawable(new TextureRegion(icon));
        buttonStyle.font = font;

        // Create a button and dynamically size it
        TextButton levelButton = new TextButton("", buttonStyle);
        levelButton.setSize(viewport.getWorldWidth() * 0.15f, viewport.getWorldHeight() * 0.15f); // 15% of screen size
        levelButton.setPosition(viewport.getWorldWidth() * positionX - levelButton.getWidth() / 2, // Dynamic X position
            viewport.getWorldHeight() / 2 - levelButton.getHeight() / 2);     // Centered vertically

        // Add click listener to navigate between levels
        levelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switch (level) {
                    case 1:
                        game.setScreen(new Level1Screen(game));
                        break;
                    case 2:
                        game.setScreen(new Level2Screen(game));
                        break;
                    case 3:
                        game.setScreen(new Level3Screen(game));
                        break;
                }
            }
        });

        stage.addActor(levelButton); // Add the button to the stage
    }

    @Override
    public void render(float delta) {
        // Apply the viewport for proper scaling
        viewport.apply(true);

        // Clear the screen
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw background
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined); // Sync with the viewport camera
        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        spriteBatch.end();

        // Draw stage elements (buttons, etc.)
        stage.act();
        stage.draw();
    }


    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true); // Update viewport and keep the aspect ratio
    }


    @Override
    public void show() { }

    @Override
    public void hide() { }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        backgroundTexture.dispose();
        level1ButtonIcon.dispose();
        level2ButtonIcon.dispose();
        level3ButtonIcon.dispose();
        font.dispose();
        stage.dispose();
    }
}








//
//package com.badlogic.drop;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Screen;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.g2d.BitmapFont;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.TextureRegion;
//import com.badlogic.gdx.scenes.scene2d.Stage;
//import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
//import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
//import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
//import com.badlogic.gdx.scenes.scene2d.InputEvent;
//import com.badlogic.gdx.utils.viewport.StretchViewport;
//import com.badlogic.gdx.utils.viewport.Viewport;
//
//public class SelectLevelScreen implements Screen {
//    private final AngryBird game;
//    private SpriteBatch spriteBatch;
//    private Texture backgroundTexture;
//    private Stage stage;
//    private BitmapFont font;
//    private Texture level1ButtonIcon;
//    private Texture level2ButtonIcon;
//    private Texture level3ButtonIcon;
//    private Viewport viewport;
//
//    public SelectLevelScreen(final AngryBird game) {
//        this.game = game;
//        spriteBatch = new SpriteBatch();
//
//        // Set the viewport to StretchViewport
//        viewport = new StretchViewport(800, 600);
//        stage = new Stage(viewport);
//        Gdx.input.setInputProcessor(stage);
//
//        // Load background texture
//        backgroundTexture = new Texture("Levelback.png");
//
//        level1ButtonIcon = new Texture("level1.png");
//        level2ButtonIcon = new Texture("level2.png");
//        level3ButtonIcon = new Texture("level3.png");
//
//        // Load the font
//        font = new BitmapFont(Gdx.files.internal("default.fnt"));
//
//        // Create buttons positioned horizontally
//        createLevelButton(level1ButtonIcon, 1, 0.3f); // Position first button at 30% of the screen width
//        createLevelButton(level2ButtonIcon, 2, 0.5f); // Position second button at 50% of the screen width
//        createLevelButton(level3ButtonIcon, 3, 0.7f); // Position third button at 70% of the screen width
//    }
//    private void createLevelButton(Texture icon, final int level, float positionX) {
//        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
//        buttonStyle.up = new TextureRegionDrawable(new TextureRegion(icon));
//        buttonStyle.down = new TextureRegionDrawable(new TextureRegion(icon));
//        buttonStyle.font = font;
//
//        TextButton levelButton = new TextButton("", buttonStyle);
//        levelButton.setSize(viewport.getWorldWidth() * 0.15f, viewport.getWorldHeight() * 0.15f);
//
//        // Position the button horizontally and center it vertically
//        levelButton.setPosition(viewport.getWorldWidth() * positionX - levelButton.getWidth() / 2,
//            viewport.getWorldHeight() / 2 - levelButton.getHeight() / 2);
//
//        levelButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                // Ensure that level screens are instantiated correctly
//                if (level == 1) {
//                    game.setScreen(new Level1Screen(game)); // Start Level 1
//                } else if (level == 2) {
//                    game.setScreen(new Level2Screen(game)); // Start Level 2
//                } else if (level == 3) {
//                    game.setScreen(new Level3Screen(game)); // Start Level 3
//                }
//            }
//        });
//
//        // Add button to stage
//        stage.addActor(levelButton);
//    }
//
//
////    private void createLevelButton(Texture icon, final int level, float positionX) {
////        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
////        buttonStyle.up = new TextureRegionDrawable(new TextureRegion(icon));
////        buttonStyle.down = new TextureRegionDrawable(new TextureRegion(icon));
////        buttonStyle.font = font;
////
////        TextButton levelButton = new TextButton("", buttonStyle);
////        levelButton.setSize(viewport.getWorldWidth() * 0.15f, viewport.getWorldHeight() * 0.15f);
////
////        // Position the button horizontally and center it vertically
////        levelButton.setPosition(viewport.getWorldWidth() * positionX - levelButton.getWidth() / 2,
////            viewport.getWorldHeight() / 2 - levelButton.getHeight() / 2);
////
////        levelButton.addListener(new ClickListener() {
////            @Override
////            public void clicked(InputEvent event, float x, float y) {
////                //game.setScreen(new MainGameScreen(game, level))
////                if(level == 1){
////                    game.setScreen(new Level1Screen(game)); // Start selected Level
////                }; // Start selected Level
////                if(level == 2){
////                    game.setScreen(new Level2Screen(game)); // Start selected Level
////                }; // Start selected Level
////                if(level == 3){
////                    game.setScreen(new Level3Screen(game)); // Start selected Level
////                }; // Start selected Level
////            }
////        });
////
////        // Add button to stage
////        stage.addActor(levelButton);
////    }
//
//    @Override
//    public void render(float delta) {
//        // Apply viewport
//        viewport.apply(true);
//
//        // Clear screen
//        Gdx.gl.glClearColor(1, 1, 1, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//        // Set the projection matrix of the SpriteBatch to match the viewport
//        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
//
//        // Draw background
//        spriteBatch.begin();
//        spriteBatch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
//        spriteBatch.end();
//
//        // Draw the stage with buttons
//        stage.act();
//        stage.draw();
//    }
//
//    @Override
//    public void resize(int width, int height) {
//        // Update viewport to new window size
//        viewport.update(width, height, true);
//    }
//
//    @Override
//    public void show() { }
//
//    @Override
//    public void hide() { }
//
//    @Override
//    public void pause() { }
//
//    @Override
//    public void resume() { }
//
//    @Override
//    public void dispose() {
//        spriteBatch.dispose();
//        backgroundTexture.dispose();
//        level1ButtonIcon.dispose();
//        level2ButtonIcon.dispose();
//        level3ButtonIcon.dispose();
//        font.dispose();
//        stage.dispose();
//    }
//}
