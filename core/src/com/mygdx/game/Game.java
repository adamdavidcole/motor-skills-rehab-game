package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Game extends com.badlogic.gdx.Game {

    public Character character;
    private CoinPath cp;

    public SpriteBatch batch;
    public BitmapFont font;

    private int height = 1280;
    private int width = 800;

    public int scrollVelocity = 200;
    public int remainingTimeSecs = 60;


    @Override
    public void create() {
        // create a Rectangle to logically represent the charShape
        character = new Character(width, height);

        batch = new SpriteBatch();
        //Use LibGDX's default Arial font.
        font = new BitmapFont();
        this.setScreen(new LoginScreen(this));


    }

    public void render() {
        super.render(); //important!
    }

    public void update() {

    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }

}

