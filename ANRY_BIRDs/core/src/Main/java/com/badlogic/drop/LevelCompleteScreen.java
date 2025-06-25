package com.badlogic.drop;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class LevelCompleteScreen implements Screen {
    private final AngryBird game;
    private final int level;
    private SpriteBatch spriteBatch;
    private Texture backgroundTexture;
    private Stage stage;

    public LevelCompleteScreen(AngryBird game, int level) {
        this.game = game;
        this.level = level;

        spriteBatch = new SpriteBatch();
        backgroundTexture = new Texture("Level_complete.jpeg"); // Ensure this file exists
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage); // Handle input through the stage

        setupUI();
    }

    private void setupUI() {
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Retry Button
        Texture retryTexture = new Texture("retry_button.png");
        ImageButton retryButton = new ImageButton(new TextureRegionDrawable(retryTexture));
        retryButton.setSize(50, 50); // Small button size
        retryButton.setPosition(200, 100); // Adjust position
        retryButton.addListener(event -> {
            if (retryButton.isPressed()) {
                game.setScreen(new Level1Screen(game)); // Restart the level
            }
            return true;
        });

        // Next Level Button
        Texture nextLevelTexture = new Texture("Next_level.jpg");
        ImageButton nextLevelButton = new ImageButton(new TextureRegionDrawable(nextLevelTexture));
        nextLevelButton.setSize(50, 50); // Small button size
        nextLevelButton.setPosition(400, 100); // Adjust position
        nextLevelButton.addListener(event -> {
            if (nextLevelButton.isPressed()) {
                if (level < 3) {
                    game.setScreen(new Level2Screen(game)); // Go to the next level
                } else {
                    game.setScreen(new HomeScreen(game, skin)); // Go back to the home screen
                }
            }
            return true;
        });

        // Add buttons to the stage
        stage.addActor(retryButton);
        stage.addActor(nextLevelButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1); // Clear screen with black background
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.end();

        stage.act(delta); // Update the stage
        stage.draw(); // Draw the stage with buttons
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        // No additional setup required
    }

    @Override
    public void hide() {
        // No additional cleanup required
    }

    @Override
    public void pause() {
        // Optional: handle pause state
    }

    @Override
    public void resume() {
        // Optional: handle resume state
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        backgroundTexture.dispose();
        stage.dispose();
    }
}
