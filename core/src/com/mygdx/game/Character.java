package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by acole9 on 4/15/15.
 */
public class Character {
    private Texture characterImage;
    private Texture characterImagePoisoned;
    public Rectangle charShape;
    public PowerContainer powers;

    private int screenWidth;
    private int screenHeight;


    public Character(int sW, int sH) {
        characterImage = new Texture(Gdx.files.internal("charactar-02.png"));
        characterImagePoisoned = new Texture(Gdx.files.internal("charactar-02-poisoned.png"));


        charShape = new Rectangle();

        // create a Rectangle to logically represent the charShape
        charShape = new Rectangle();
        //System.out.println("height: " + characterImage.getHeight() + ", " + characterImage.getWidth());
        charShape.width = (int)(characterImage.getWidth() *.42);
        charShape.height = (int)(characterImage.getHeight()*.42);
        charShape.x = sW / 2 - charShape.width / 2; // center the charShape horizontally
        charShape.y = sH - charShape.height - 50; // bottom left corner of the charShape is 20 pixels above the bottom screen edge

        powers = new PowerContainer(screenWidth, screenHeight);

        screenWidth = sW;
        screenHeight = sH;
    }

    public void render(SpriteBatch batch) {
        if (powers.isPoisoned()) {
            batch.draw(characterImagePoisoned, charShape.x, charShape.y, charShape.width, charShape.height);
        }
        else batch.draw(characterImage, charShape.x, charShape.y, charShape.width, charShape.height);
    }

    public void setX(int x) {
        charShape.x = x;
    }

    public int getX(){
        return (int) charShape.x;
    }

    public int getWidth() {
        return (int) charShape.width;
    }

    public void update() {
        // update position using touch down position
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            charShape.setX((int)(touchPos.x - charShape.getWidth() / 2));
        }

        // update position using keypad
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && powers.isPoisoned())
            charShape.setX((int)(charShape.getX() + 200 * Gdx.graphics.getDeltaTime()));
        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            charShape.setX((int)(charShape.getX() - 200 * Gdx.graphics.getDeltaTime()));
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && powers.isPoisoned())
            charShape.setX((int)(charShape.getX() - 200 * Gdx.graphics.getDeltaTime()));
        else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            charShape.setX((int)(charShape.getX() + 200 * Gdx.graphics.getDeltaTime()));

        // make sure character stays within bounds of screen
        if (charShape.getX() < 0)
            charShape.setX(0);
        if (charShape.getX() > screenWidth - charShape.getWidth())
            charShape.setX(screenWidth - charShape.getWidth());

       powers.update();
    }

}
