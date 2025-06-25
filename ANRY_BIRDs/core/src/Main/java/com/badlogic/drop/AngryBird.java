
package com.badlogic.drop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class AngryBird extends Game {
    private SpriteBatch batch;
    private Sound backgroundSound;

    @Override
    public void create() {
        batch = new SpriteBatch();
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.setScreen(new HomeScreen(this, skin)); // Directly set to HomeScreen
        backgroundSound = Gdx.audio.newSound(Gdx.files.internal("angry_birds.mp3"));
        backgroundSound.loop();
    }

    @Override
    public void dispose() {
        batch.dispose();
        backgroundSound.dispose();
    }

    public SpriteBatch getBatch() {
        return batch;
    }
}
