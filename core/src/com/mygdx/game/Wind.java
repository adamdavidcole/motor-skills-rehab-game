package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * A class that keeps track of all the wind graphics and entering/
 */
public class Wind {

    public Boolean windActive = false;
    public Boolean windBlowing = false;
    public int windDuration = 0;
    public Texture wAlert = new Texture(Gdx.files.internal("windAlert.png"));

    private Texture texture = new Texture(Gdx.files.internal("wind-entering.png"));  // image of wind
    private Rectangle wind;             // physical representation of wind
    private Sound windSound;    // blowing sound of wind
    private int width;                  // width of wind
    private int height;                 // height of wind
    private boolean draw;
    private long lastTimeSpawned;       // last time spawned
    private double strength = 5000;   // adjusted based off game difficulty
    private int numberOfStepsToEnter = 0; // counter to keep track of wind entering
    private int numberOfStepsToExit = 0;  // counter ot keep track of wind exiting
    private int startingX;                  // starting location for wind on right
    private boolean onRight = false; // whether wind is on left or right

    // Constructor to create a wind object
    public Wind(int screenWidth, int screenHeight) {
        startingX = screenWidth;
        width = (int)(.3*texture.getWidth());
        height = (int)(.3*texture.getHeight());
        wind = new Rectangle();
        wind.x = screenWidth;
        wind.y = screenHeight - height - 50;
        wind.width = width;
        wind.height = height;
        draw = true;
        lastTimeSpawned = TimeUtils.millis();
        strength = 5000 + 1000*GameState.difficultySetting;

    }

    // called in GameScreen to show where the alert should display (on left or right)
    public int windAlertLocWidth() {
        if (onRight) {
            return GameState.GAME_WORLD_WIDTH - wAlert.getWidth() - 50;
        } else {
            return wAlert.getWidth() + 50;
        }
    }

    // called in GameScreen to show where the alert should display
    public int windAlertLocHeight() {
        return GameState.GAME_WORLD_HEIGHT - wAlert.getHeight() - 80;
    }

    public Texture getTexture() {
        return texture;
    }

    public Rectangle getRectangle() {
        return wind;
    }

    // returns whether the cloud is entering right now and updates position
    public boolean entering() {
        if (numberOfStepsToEnter != 0) {
            numberOfStepsToEnter--;
            if (onRight) {
                wind.x -= 1;
            } else {
                wind.x += 1;
            }
            return true;
        } else {
            return false;
        }
    }

    // determines if cloud is coming on left or right and resets some values to prepare
    public void prepare() {
        if (Math.random() <= .5) { // comes from right
            wind.x = startingX;
            onRight = true;
            // set entering texture
            texture = new Texture(Gdx.files.internal("wind-entering.png"));
        } else {
            onRight = false;
            wind.x = 0 - wind.getWidth();
            // set entering texture
            texture = new Texture(Gdx.files.internal("wind-entering-left.png"));
        }
    }

    // starts the cloud entering process by setting a numberOfStepsToEnter
    public void enter() {
        // reset values
        numberOfStepsToEnter = 12000;
        numberOfStepsToExit = 0;
    }

    // returns whether the cloud is exiting and updates position
    public boolean exiting() {
        if (numberOfStepsToExit != 0) {
            numberOfStepsToExit--;
            if (onRight) {
                wind.x += 1;
            } else {
                wind.x -= 1;
            }
            return true;
        } else {
            return false;
        }
    }

    // prepares the cloud to exit
    public void windExit(){
        // reset the exit steps
        numberOfStepsToExit = 12000;
        numberOfStepsToEnter = 0;
        // set retreating texture
        if (onRight) {
            texture = new Texture(Gdx.files.internal("wind-exiting.png"));
        } else {
            texture = new Texture(Gdx.files.internal("wind-exiting-left.png")); //TODO
        }
    }

    // returns how long wind will blow for
    public int update() {
        if (TimeUtils.millis() - lastTimeSpawned > 15000 && Math.random() > .996) {
            // update Time
            lastTimeSpawned=TimeUtils.millis();
            return (int)((strength*Math.random())+strength);
        }
        return 0;
    }

    // starts playing the wind sound effect
    public void playSound() {
        if (onRight) {
            // set blowing texture
            texture = new Texture(Gdx.files.internal("wind-blowing.png"));
        } else {
            texture = new Texture(Gdx.files.internal("wind-blowing-left.png"));
        }
        // play wind sound
        windSound = Gdx.audio.newSound(Gdx.files.internal("windSound.wav"));
        windSound.play();
    }

    // stops playing the wind sound effect
    public void stopSound() {
        if (onRight) {
            // set retreating texture
            texture = new Texture(Gdx.files.internal("wind-exiting.png"));
        } else {
            texture = new Texture(Gdx.files.internal("wind-exiting-left.png"));
        }
        // stop wind sound
        windSound.dispose();
    }

    public void doneExiting() {
        numberOfStepsToExit=0;
    }

    public void render(SpriteBatch batch) {
        if (draw) batch.draw(texture, wind.x, wind.y, width, height);

    }

    // returns the amount the character should be pushed by wind (1 and -1 represent normal character movement)
    public double shiftAmount(){
        if (onRight) {
            return -.3;
        }
        return .3;
    }

    // removes media files for memory
    public void dispose() {
        draw = false;
        texture.dispose();
        windSound.dispose();
    }

    public long getTimeSpawned() {
        return lastTimeSpawned;
    }

}
