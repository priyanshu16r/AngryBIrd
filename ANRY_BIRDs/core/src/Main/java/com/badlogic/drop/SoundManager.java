package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {
    private Music backgroundMusic;

    public SoundManager() {
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("background_music.mp3"));
        backgroundMusic.setLooping(true);
    }

    // Play background music
    public void playBackgroundMusic() {
        if (!backgroundMusic.isPlaying()) {
            backgroundMusic.play();
        }
    }

    // Stop background music
    public void stopBackgroundMusic() {
        if (backgroundMusic.isPlaying()) {
            backgroundMusic.stop();
        }
    }

    // Dispose of sounds
    public void dispose() {
        backgroundMusic.dispose();
    }
}
