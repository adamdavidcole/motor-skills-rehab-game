package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Created by gabriel on 4/23/15.
 */
public class Wind {
    // TODO: NEEDS TO BE REPLACED WITH ACTUAL PNG IMAGE INSTEAD OF ALERT
    Texture texture = new Texture(Gdx.files.internal("windAlert.png"));  // image of wind
    Rectangle wind;           // physical representation of wind
    int width;                  // width of wind
    int height;                 // height of wind
    boolean draw;
    long timeSpawned;           // last time spawned

    public Wind() {
        width = (int)(texture.getWidth() * 0.1);
        height = (int)(texture.getHeight() * 0.1);
        wind = new Rectangle();
        wind.x = (int)(Math.random() * 800);
        wind.y = 0;
        wind.width = width;
        wind.height = height;
        draw = true;
        timeSpawned = TimeUtils.millis();

    }


    public Texture getTexture() {
        return texture;
    }

    public int getX() {
        return (int) wind.x;
    }

    public int getY() {
        return (int) wind.y;
    }

    public int getW() {
        return width;
    }

    public int getH() {
        return height;
    }


    public Rectangle getRectangle() {
        return wind;
    }

    // signifies the animation for cloud entering should appear
    public void enter() {
        // set entering texture
        wind.x += 1;
    }

    // wind is currently blowing
    public void blow() {
        // set blowing texture
        // play wind sound

    }

    // wind is retreating
    public void windExit() {
        // turn off blowing temperature
        // change texture

    }

    public void render(SpriteBatch batch) {
        if (draw) batch.draw(texture, wind.x, wind.y, width, height);

    }

    public void dispose() {
        draw = false;
        texture.dispose();
    }

    public long getTimeSpawned() {
        return timeSpawned;
    }

}
