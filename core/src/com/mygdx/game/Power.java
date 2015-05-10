package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * This is an abstract super class for a "power" up or down. The class exposes a render
 * function as well as functions to get the "power height, width, and position.
 */

public abstract class Power {
    Texture texture;            // image of power
    Rectangle power;            // physical representation of power
    int width;                  // width of power
    int height;                 // height of power
    boolean draw;
    long timeSpawned;           // last time spawned
    float multiplier;


    // Constructor that sets initial position, width, height and position of power up/down
    public Power() {
        power = new Rectangle();
        power.x = (int)(Math.random() * 800);
        //power.y = -height;
        draw = true;
        timeSpawned = TimeUtils.millis();
        // default multiplier is 1
        multiplier = 1;
    }

    public void initTexture(Texture texture, double scaleFactor) {
        this.texture = texture;
        width = (int)(texture.getWidth() * scaleFactor);
        height = (int)(texture.getHeight() * scaleFactor);
        power.y = -height;
        power.width = width;
        power.height = height;
    }

    public Texture getTexture() {
        return texture;
    }

    public int getX() { return (int) power.x; }

    public int getY() {
        return (int) power.y;
    }

    public int getW() {
        return width;
    }

    public int getH() {
        return height;
    }

    public Rectangle getRectangle() {
        return power;
    }

    public void update() { power.y += 2; }

    public void update(int speed) {
    }

    public void render(SpriteBatch batch) {
        if (draw) batch.draw(texture, power.x, power.y, width, height);
    }

    public void dispose() {
        draw = false;
        texture.dispose();
    }

    public long getTimeSpawned() {
        return timeSpawned;
    }

    public void setTimeCollected() {
        timeSpawned = TimeUtils.millis();
    }

    public float getMultiplier() {
        return multiplier;
    }

}
