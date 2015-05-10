package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

public abstract class Obstacle {
    Texture texture;            // image of obstacle
    Rectangle obstacle;         // physical representation of obstacle
    int width;                  // width of obstacle
    int height;                 // height of obstacle
    boolean draw;
    long timeSpawned;           // last time spawned


    // Constructor that sets initial position, width, height and position of obstacle
    public Obstacle() {
        obstacle = new Rectangle();
        obstacle.x = (int)(Math.random() * 800);
        //power.y = -height;
        draw = true;
        timeSpawned = TimeUtils.millis();
    }

    public void initTexture(Texture texture, double scaleFactor) {
        this.texture = texture;
        width = (int)(texture.getWidth() * scaleFactor);
        height = (int)(texture.getHeight() * scaleFactor);
        obstacle.y = -height;
        obstacle.width = width;
        obstacle.height = height;
    }

    public Texture getTexture() {
        return texture;
    }

    public int getX() { return (int) obstacle.x; }

    public int getY() {
        return (int) obstacle.y;
    }

    public int getW() {
        return width;
    }

    public int getH() {
        return height;
    }

    public Rectangle getRectangle() {
        return obstacle;
    }

    public void update() { obstacle.y += 9; }

    public void update(int speed) {
    }

    public void render(SpriteBatch batch) {
        if (draw) batch.draw(texture, obstacle.x, obstacle.y, width, height);
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


}