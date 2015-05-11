package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import java.util.LinkedList;
import java.util.List;

/**
 * This class models the game character and handles movement, collisions and rendering
 */
public class Character {

    private Texture characterImage;          // image of character
    private Texture characterImageLeanRight;          // image of character tilted right
    private Texture characterImageLeanLeft;          // image of character tilted left
    private Texture characterImagePoisoned;  // image of character poisoned
    public Rectangle charShape;              // physical representation of character
    public PowerContainer powers;            // container of powers character has

    private float CHARACTER_SCALE_FACTOR = 0.4f; // scale factor for character images
    private int VERTICAL_POS_OFFSET = 75;         // space above character and below top of screen
    private int MAX_RANGE_OF_MOTION = 5;          // maximum range of motion setting

    private boolean transperent = false;      // whether character can collect
    private int collisionCounter;             // tracks how long character should remain transparent
    // orientation of character image to be rendered
    public characterOrientation orientation = characterOrientation.NONE;

    /**
     * Character constructor creates character textures, shapes, and power container
     * @param sW - screen width
     * @param sH - screen height
     */
    public Character(int sW, int sH) {
        // create textures for character images
        characterImage = new Texture(Gdx.files.internal("charactar4-01.png"));
        characterImageLeanRight = new Texture(Gdx.files.internal("charactar4-right.png"));
        characterImageLeanLeft = new Texture(Gdx.files.internal("charactar4-left.png"));
        characterImagePoisoned = new Texture(Gdx.files.internal("charactar-02-poisoned.png"));

        // create a Rectangle to logically represent the charShape
        charShape = new Rectangle();
        charShape.width = (int)(characterImage.getWidth() - characterImage.getWidth()
                * CHARACTER_SCALE_FACTOR);
        charShape.height = (int)(characterImage.getHeight() - characterImage.getHeight()
                * CHARACTER_SCALE_FACTOR);
        // center the charShape horizontally
        charShape.x = sW / 2 - charShape.width / 2;
        // bottom left corner of the charShape is vertically offset from top of screen
        charShape.y = sH - charShape.height - VERTICAL_POS_OFFSET;

        powers = new PowerContainer(sW, sH);
    }

    public void collision(){
        collisionCounter = 300;
        transperent = true;
    }

    public boolean isTransperent(){
        return transperent;
    }


    /**
     * Render appropriate image for character depending on state. Image shifts left and right
     * if arrow keys pressed to mimick falling air balloon effect.
     * @param batch
     */

    public void render(SpriteBatch batch) {

        if (collisionCounter > 0){
           collisionCounter--;
            if (collisionCounter%10 != 0) {
                return;
            }
        } else {
            transperent = false;
        }


        if (powers.isPoisoned()) {
            batch.draw(characterImagePoisoned, charShape.x, charShape.y);
        } else if (orientation == characterOrientation.LEFT) {
            batch.draw(characterImageLeanLeft, charShape.x, charShape.y);
        } else if (orientation == characterOrientation.RIGHT) {
            batch.draw(characterImageLeanRight, charShape.x, charShape.y);
        }
        else batch.draw(characterImage, charShape.x, charShape.y);
    }

    /**
     * Returns current horizontal position of x
     */
    public int getX(){
        return (int) charShape.x;
    }

    /**
     * Sets x position of character
     */
    public void setHorizontalPos(int x){
        charShape.x = x;
    }

    /**
     * Sets y position of character
     */
    public void setVerticalPos(int y){
        charShape.y = y;
    }

    /**
     * Returns width of character shape
     */
    public int getWidth() {
        return (int) charShape.width;
    }

    /**
     * Shift character's horizontal position x spots relative to current position. Amount shifted
     * is dependent on user's range of motion.
     */
    public void shiftCharacterHorizontally(float x) {
        // velocity of character is calculated using range of motion
        int shiftVelocity = MAX_RANGE_OF_MOTION - GameState.rangeOfMotionSetting;
        // Shift character horizontally according to velocity
        charShape.setPosition(charShape.x + x * shiftVelocity,charShape.y);
    }

    /**
     * Shift character's vertical position y spots relative to current position.
     */
    public void shiftCharacterVertically(float y) {
        charShape.setPosition(charShape.x, charShape.y - y);
    }

    /**
     * Update state of character including position, orientation and powers
     */
    public void update() {
        // update position using touch down position - for testing purposes only
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            charShape.setPosition((int) (touchPos.x - charShape.width / 2), charShape.y);
        }

        // update position and orientation of character by one unit using keypad
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && powers.isPoisoned()) {
            shiftCharacterHorizontally(1);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            shiftCharacterHorizontally(-1);
            orientation = characterOrientation.LEFT;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && powers.isPoisoned()) {
            shiftCharacterHorizontally(-1);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            shiftCharacterHorizontally(1);
            orientation = characterOrientation.RIGHT;
        } else orientation = characterOrientation.NONE;


        // make sure character stays within bounds of screen
        if (charShape.x < 0)
            charShape.x = 0;
        if (charShape.x > GameState.GAME_WORLD_WIDTH - charShape.width*1.5)
            charShape.x = GameState.GAME_WORLD_WIDTH - charShape.width*1.5f;

       powers.update();
    }

}
