package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.awt.Rectangle;

/**
 * Created by acole9 on 4/15/15.
 */
public class Background {
    private Texture bgImage;
    private Rectangle bgShape;
    private int screenWidth;
    private int screenHeight;

    public Background(int sW, int sH) {
        bgImage = new Texture(Gdx.files.internal("cloudBG.png"));
        bgShape = new Rectangle();
        screenWidth = sW;
        screenHeight = sH;
        bgShape.x = screenWidth/2;
        bgShape.y = screenHeight;
        bgShape.width = screenWidth;
        bgShape.height = bgImage.getHeight();
    }

    public void render(SpriteBatch batch) {
        batch.draw(bgImage, bgShape.x, bgShape.y);
    }
}
