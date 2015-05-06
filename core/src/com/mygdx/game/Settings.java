package com.mygdx.game;

/**
 * Created by acole9 on 5/6/15.
 */
public class Settings {
    private int difficulty;
    public int gameDuration;
    private int rangeOfMotion;
    private static Settings instance;

    private Settings() {
        difficulty = 1;
        gameDuration = 15*60;
        rangeOfMotion = 1;
    }

    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }
}
