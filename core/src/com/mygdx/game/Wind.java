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
    // TODO: NEEDS TO BE REPLACED WITH ACTUAL PNG IMAGE INSTEAD OF ALERT
    Texture texture = new Texture(Gdx.files.internal("wind-entering.png"));  // image of wind
    Rectangle wind;             // physical representation of wind
    private Sound windSound;    // blowing sound of wind
    int width;                  // width of wind
    int height;                 // height of wind
    boolean draw;
    long lastTimeSpawned;       // last time spawned
    double strength = 10000;   // for later calibration on strength or duration of wind
    int numberOfStepsToEnter = 0;
    int numberOfStepsToExit = 0;
    int startingX;

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
            //if (numberOfStepsToEnter%150 == 0) {
                wind.x -= 1;
            //}
            return true;
        } else {
            return false;
        }
    }

    // prepares the cloud to enter
    public void enter() {
        // reset the entry steps
        numberOfStepsToEnter = 12000;
        //reset values
        wind.x = startingX;
        numberOfStepsToExit = 0;
        // set entering texture
         texture = new Texture(Gdx.files.internal("wind-entering.png"));
    }

    // returns whether the cloud is exiting and updates position
    public boolean exiting() {
        if (numberOfStepsToExit != 0) {
            numberOfStepsToExit--;
                wind.x += 1;
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
        texture = new Texture(Gdx.files.internal("wind-exiting.png"));
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
        // set blowing texture
         texture = new Texture(Gdx.files.internal("wind-blowing.png"));
        // play wind sound
        windSound = Gdx.audio.newSound(Gdx.files.internal("windSound.wav"));
        windSound.play();
    }

    public void stopSound() {
        // set retreating texture
         texture = new Texture(Gdx.files.internal("wind-exiting.png"));
        // stop wind sound
        windSound.dispose();
    }

    public void doneExiting() {
        numberOfStepsToExit=0;
    }

    public void render(SpriteBatch batch) {
        if (draw) batch.draw(texture, wind.x, wind.y, width, height);

    }

    public void dispose() {
        draw = false;
        texture.dispose();
    }

    public long getTimeSpawned() {
        return lastTimeSpawned;
    }

}
