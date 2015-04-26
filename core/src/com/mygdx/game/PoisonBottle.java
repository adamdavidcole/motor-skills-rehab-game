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
public class PoisonBottle extends Power {

    public PoisonBottle() {
        texture = new Texture(Gdx.files.internal("poisonbottle.png"));
        multiplier = 0.5f;
        super.initTexture(texture, 0.1);
    }

}
