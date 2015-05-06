package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * Created by acole9 on 5/6/15.
 */
public class Soundtrack {
    public Music gameMusic;
    private int NUM_SONGS = 1;

    public Soundtrack() {
        generateMusic();
    }

    public void generateMusic() {
        int rand = (int)(Math.random() * NUM_SONGS) + 1;
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("song" + rand + ".mp3"));
        gameMusic.setLooping(true);
        /*gameMusic.setOnCompletionListener(new Music.OnCompletionListener(){
            @Override
            public void onCompletion(Music aMusic){
                generateMusic();
            }
        });*/
    }
}
