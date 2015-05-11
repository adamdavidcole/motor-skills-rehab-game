package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Created by gabriel on 4/23/15.
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
    private int numberOfStepsToEnter = 0;
    private int numberOfStepsToExit = 0;
    private int startingX;
    private boolean onRight = false;

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

    public int windAlertLocWidth() {
        if (onRight) {
            return GameState.GAME_WORLD_WIDTH - wAlert.getWidth() - 50;
        } else {
            return wAlert.getWidth() + 50;
        }
    }

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

    // prepares the cloud to enter
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

    public int update() {
        if (TimeUtils.millis() - lastTimeSpawned > 15000 && Math.random() > .996) {
            // update Time
            lastTimeSpawned=TimeUtils.millis();
            return (int)((strength*Math.random())+strength);
        }
        return 0;
    }


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

    public double shiftAmount(){
        if (onRight) {
            return -.3;
        }
        return .3;
    }

    public void dispose() {
        draw = false;
        texture.dispose();
    }

    public long getTimeSpawned() {
        return lastTimeSpawned;
    }

}
