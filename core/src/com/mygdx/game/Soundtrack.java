package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * Instantiates the a random game song from soundtrack
 */
public class Soundtrack {
    public Music gameMusic; // current game music song
    private int NUM_SONGS = 1;  // number of songs availible on soundtrack

    public Soundtrack() {
        generateMusic();
    }

    public void generateMusic() {
        // choose a random song value in range of NUM_SONGS
        int rand = (int)(Math.random() * NUM_SONGS) + 1;
        // play the random song
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("song" + rand + ".mp3"));
        // loop song
        gameMusic.setLooping(true);
        // On completion of song action listener
        /*gameMusic.setOnCompletionListener(new Music.OnCompletionListener(){
            @Override
            public void onCompletion(Music aMusic){
                generateMusic();
            }
        });*/
    }
}
