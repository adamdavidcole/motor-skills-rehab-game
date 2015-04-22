package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * This function implements a power up/down. Specifically, this power down
 * causes the character to move opposite to expected directions.
 */
public class PoisonBottle implements Power {
    Texture texture = new Texture(Gdx.files.internal("poisonbottle.png"));  // image of bottle
    Rectangle bottle;           // physical representation of bottle
    int width;                  // width of bottle
    int height;                 // height of bottle
    boolean draw;
    long timeSpawned;           // last time spawned
    float multiplier;


    // Constructor that sets initial position, width, height and posititon of power up/down
    public PoisonBottle() {
        width = (int)(texture.getWidth() * 0.1);
        height = (int)(texture.getHeight() * 0.1);
        bottle = new Rectangle();
        bottle.x = (int)(Math.random() * 800);
        bottle.y = 0;
        bottle.width = width;
        bottle.height = height;
        draw = true;
        timeSpawned = TimeUtils.millis();
        multiplier = 0.5f;
    }


    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public int getX() {
        return (int) bottle.x;
    }

    @Override
    public int getY() {
        return (int) bottle.y;
    }

    @Override
    public int getW() {
        return width;
    }

    @Override
    public int getH() {
        return height;
    }


    @Override
    public Rectangle getRectangle() {
        return bottle;
    }

    public void update() {
        bottle.y += 2;
    }

    @Override
    public void update(int speed) {

    }

    @Override
    public void render(SpriteBatch batch) {
        if (draw) batch.draw(texture, bottle.x, bottle.y, width, height);

    }

    public void dispose() {
        draw = false;
        texture.dispose();
    }

    public long getTimeSpawned() {
        return timeSpawned;
    }

    @Override
    public void setTimeCollected() {
        timeSpawned = TimeUtils.millis();
    }

    public float getMultiplier() {
        return multiplier;
    }
}
