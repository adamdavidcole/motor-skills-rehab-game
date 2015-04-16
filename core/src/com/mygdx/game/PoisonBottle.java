package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Created by acole9 on 4/15/15.
 */
public class PoisonBottle implements Power {
    Texture texture = new Texture(Gdx.files.internal("poisonbottle.png"));
    Rectangle bottle;
    int width;
    int height;
    boolean draw;
    long timeSpawned;

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
}
