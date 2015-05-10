package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Times 2 multiplier power up doubles the score of the user
 */
public class Times2Multiplier extends Power {

    public Times2Multiplier() {
        texture = new Texture(Gdx.files.internal("x2multiplier.png"));
        multiplier = 2f;
        super.initTexture(texture, 1);
    }

}