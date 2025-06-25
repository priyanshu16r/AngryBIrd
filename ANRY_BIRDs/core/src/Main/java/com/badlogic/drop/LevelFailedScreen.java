package com.badlogic.drop;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class LevelFailedScreen implements Screen {
    private final AngryBird game;
    private final int level;
    private SpriteBatch spriteBatch;
    private Texture backgroundTexture;

    private Stage stage;
    private Viewport viewport;
    private Skin skin;

    public LevelFailedScreen(AngryBird game, int level, Skin skin) {
        this.game = game;
        this.level = level;
        this.skin = skin;
        this.spriteBatch = new SpriteBatch();
        this.backgroundTexture = new Texture("level_failed.jpg"); // Ensure the texture file exists

        // Set up Stage and Viewport
        viewport = new FitViewport(800, 600); // Adjust as per your screen dimensions
        stage = new Stage(viewport, spriteBatch);
        Gdx.input.setInputProcessor(stage);

        // Create buttons
        createButtons();
    }

    private void createButtons() {
        // Retry Button
        TextButton retryButton = new TextButton("Retry", skin);
        retryButton.setSize(100, 50); // Small button
        retryButton.setPosition(350, 200); // Adjust as per your layout
        retryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new Level1Screen(game)); // Retry Level 1
            }
        });

        // Back Button
        TextButton backButton = new TextButton("Back", skin);
        backButton.setSize(100, 50); // Small button
        backButton.setPosition(350, 100); // Adjust as per your layout
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SelectLevelScreen(game)); // Go back to level selection
            }
        });

        // Add buttons to the stage
        stage.addActor(retryButton);
        stage.addActor(backButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1); // Clear screen with black background
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw background
        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        spriteBatch.end();

        // Draw Stage (buttons)
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        backgroundTexture.dispose();
        stage.dispose();
    }
}
