package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class HomeScreen implements Screen {
    private final AngryBird game;
    private Stage stage;
    private TextButton startGameButton;
    private TextButton selectLevelButton;
    private SpriteBatch spriteBatch;
    private Texture backgroundTexture;
    private Skin skin;
    private BitmapFont font;
    private Texture startGameIcon;
    private Texture selectLevelIcon;

    public HomeScreen(final AngryBird game, final Skin skin) {
        this.game = game;
        this.skin = skin;
        spriteBatch = new SpriteBatch();
        backgroundTexture = new Texture("home_bg.png");

        stage = new Stage(new StretchViewport(800, 600));
        Gdx.input.setInputProcessor(stage);

        // Load button icons
        startGameIcon = new Texture("Start.png");
        selectLevelIcon = new Texture("levels.png");

        // Load the font
        font = new BitmapFont(Gdx.files.internal("default.fnt"));

        // Create buttons
        selectLevelButton = createButton("", selectLevelIcon, 80); // Positioned closer to the bottom
        startGameButton = createButton("", startGameIcon, 160); // Positioned just above select level button

        // Add button listeners
        startGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new Level1Screen(game));
            }
        });


        selectLevelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SelectLevelScreen(game)); // Go to level selection screen
            }
        });

        // Add buttons to stage
        stage.addActor(startGameButton);
        stage.addActor(selectLevelButton);
    }

    private TextButton createButton(String text, Texture icon, float positionY) {
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = new TextureRegionDrawable(new TextureRegion(icon));
        buttonStyle.down = new TextureRegionDrawable(new TextureRegion(icon));
        buttonStyle.font = font;

        TextButton button = new TextButton(text, buttonStyle);
        button.setSize(200, 60); // Set a larger size for better visibility
        button.setPosition((800 - button.getWidth()) / 2, positionY); // Center horizontally with adjusted vertical position

        return button;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw background
        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.end();

        // Draw the stage with buttons
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        // Dispose resources
        stage.dispose();
        spriteBatch.dispose();
        backgroundTexture.dispose();
        startGameIcon.dispose();
        selectLevelIcon.dispose();
        font.dispose(); // Dispose the font
    }
}
