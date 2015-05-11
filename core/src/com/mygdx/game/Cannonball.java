package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * This function implements obstacles. Obstacles can either push or damage, for now
 * cannonballs damage (cause character to blink and unable to pick up coins)
 */

public class Cannonball extends Obstacle {

    public Cannonball() {
        texture = new Texture(Gdx.files.internal("cannonball.png"));
        super.initTexture(texture, 1);
    }

}
