package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;

/**
 * This interface defines a "power" up or down. The interface exposes a render
 * function as well as functions to get the "power height, width, and position.
 *
 */

/**
 * Created by acole9 on 4/15/15.
 */
public interface Power {
    Texture getTexture();
    int getX();
    int getY();
    int getW();
    int getH();
    Rectangle getRectangle();
    void update();
    void update(int speed);
    void render(SpriteBatch batch);
    long getTimeSpawned();
    void setTimeCollected();
    void dispose();
}
