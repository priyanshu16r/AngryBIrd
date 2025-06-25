package com.badlogic.drop;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class PauseScreen implements Screen {
    private final AngryBird game;
    private SpriteBatch batch;
    private Texture pauseBackground;
    private Stage stage;
    private TextButton resumeButton;
    private TextButton homeButton;
    private TextButton exitButton;
    private Texture pauseSymbol;
    private Texture resumeIcon;
    private Texture homeIcon;
    private Texture exitIcon;
    private BitmapFont font;
    private StretchViewport viewport;

    public PauseScreen(final AngryBird game, final Skin skin,int level) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.viewport = new StretchViewport(800, 600);
        this.stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        pauseBackground = new Texture("Background_pause_screen.jpg");
        pauseSymbol = new Texture("pause_symbol.png");
        resumeIcon = new Texture("Resume.png");
        homeIcon = new Texture("homebutton.png");
        exitIcon = new Texture("Untitled-2.png");

        font = new BitmapFont(Gdx.files.internal("default.fnt"));

        // Button styles
        TextButton.TextButtonStyle resumeButtonStyle = new TextButton.TextButtonStyle();
        resumeButtonStyle.up = new TextureRegionDrawable(new TextureRegion(resumeIcon));
        resumeButtonStyle.down = new TextureRegionDrawable(new TextureRegion(resumeIcon));
        resumeButtonStyle.font = font;

        TextButton.TextButtonStyle homeButtonStyle = new TextButton.TextButtonStyle();
        homeButtonStyle.up = new TextureRegionDrawable(new TextureRegion(homeIcon));
        homeButtonStyle.down = new TextureRegionDrawable(new TextureRegion(homeIcon));
        homeButtonStyle.font = font;

        TextButton.TextButtonStyle exitButtonStyle = new TextButton.TextButtonStyle();
        exitButtonStyle.up = new TextureRegionDrawable(new TextureRegion(exitIcon));
        exitButtonStyle.down = new TextureRegionDrawable(new TextureRegion(exitIcon));
        exitButtonStyle.font = font;

        // Initialize buttons
        resumeButton = new TextButton("", resumeButtonStyle);
        resumeButton.setSize(200, 60);
        resumeButton.setPosition((800 - resumeButton.getWidth()) / 2, 350);
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(level==1)
                    game.setScreen(new Level1Screen(game));
                else if(level==2)
                    game.setScreen(new Level2Screen(game));
                else if(level==3)
                    game.setScreen(new Level3Screen(game));
            }
        });

        homeButton = new TextButton("", homeButtonStyle);
        homeButton.setSize(200, 60);
        homeButton.setPosition((800 - homeButton.getWidth()) / 2, 250);
        homeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new HomeScreen(game, skin));
            }
        });

        exitButton = new TextButton("", exitButtonStyle);
        exitButton.setSize(200, 60);
        exitButton.setPosition((800 - exitButton.getWidth()) / 2, 150);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        // Add buttons to stage
        stage.addActor(resumeButton);
        stage.addActor(homeButton);
        stage.addActor(exitButton);
    }

    @Override
    public void render(float delta) {
        viewport.apply();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw pause screen background
        batch.begin();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.draw(pauseBackground, 0, 0, 800, 600);
        batch.end();

        // Draw the stage with buttons
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void show() {}

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        batch.dispose();
        pauseBackground.dispose();
        stage.dispose();
        pauseSymbol.dispose();
        resumeIcon.dispose();
        homeIcon.dispose();
        exitIcon.dispose();
        font.dispose();
    }
}
