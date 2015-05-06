package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Rainbow powerup spawns a pot of gold worth 50 coins!
 */
public class Rainbow extends Power {
    public Rainbow(){
        texture = new Texture(Gdx.files.internal("rainbow.gif"));
        multiplier = 1;
        super.initTexture(texture, .15);
    }

}

