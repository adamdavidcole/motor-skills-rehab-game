package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.awt.Point;
import java.util.Iterator;

/**
 * A class to represent the optimal path along which coins spawn
 * the optimal path is a sinusoid centered on the screen with a randomly varying amplitude every
 * half-period
 */
public class OptimalPath {
    // width and height of the screen on which the path exists
    private int screenWidth;
    private int screenHeight;

    // sinusoidal path paramters
    private final double PI = Math.PI;
    private double period;
    private int amplitude;
    private double t0;
    private boolean inSecondHalfPeriod;

    // reference points for comparing optimal path vs. actual path
    private Array<Point> refPoints;
    private double lastRefPointTime;
    private final double SAMPLE_INTERVAL = 500000000.;  // nanoseconds

    private int maxXRange;
    private int minXRange;

    public DataFile dataFile;


    public OptimalPath(int sW, int sH, DataFile df) {
        screenWidth = sW;
        screenHeight = sH;

        refPoints = new Array<Point>();

        amplitude = randomAmplitude();
        period = 4000000000.; // nanoseconds
        t0 = (double) TimeUtils.nanoTime();
        inSecondHalfPeriod = false;

        maxXRange = sW / 2;
        minXRange = sW / 2;

        // spawn the first reference point
        spawnRefPoint();

        dataFile = df;
    }

    // computes a new random amplitude
    private int randomAmplitude() {
        return MathUtils.random(0, (screenWidth - CoinPath.COIN_WIDTH) / 2);
    }

    // computes the optimal path centered on the screen and randomizes the amplitude every half-period
    public float computeOptimalPath() {
        double t = (double) TimeUtils.nanoTime() - t0;
        // randomize amplitude every half-period
        if (t > period/2 && !inSecondHalfPeriod) {
            inSecondHalfPeriod = true;
            amplitude = randomAmplitude();
        } else if (t > period) {
            inSecondHalfPeriod = false;
            amplitude = randomAmplitude();
            t0 = (double) TimeUtils.nanoTime();
        }
        double offset = screenWidth / 2;
        double argument = 2*PI*t/period;

        float xPosition = (float) (offset + amplitude*Math.sin(argument));
        return xPosition;
    }

    // moves reference points up on the screen as the screen scrolls
    // when a reference point reaches the height of the character, writes the optimal path position
    // and the actual position of the character to a .CSV file
    public void updateOptimalPath(Rectangle charShape) {
        if (TimeUtils.nanoTime() - lastRefPointTime > SAMPLE_INTERVAL) {
            spawnRefPoint();
        }
        // move the reference points, removing them as they pass the character while ex
        Iterator<Point> iter = refPoints.iterator();
        while (iter.hasNext()) {
            Point rp = iter.next();
            rp.y += GameState.gameScrollSpeed * Gdx.graphics.getDeltaTime();
            if (rp.y >= charShape.y) {
                float charMiddle = charShape.x + charShape.getWidth()/2;
                writeToCSV(rp.x, charMiddle);
                iter.remove();
            }
        }
    }

    // spawns a point on the optimal path (calculated at the bottom of the screen) to use as a
    // reference for when it reaches the character's height on the screen
    private void spawnRefPoint() {
        Point rp = new Point();
        rp.x = (int) computeOptimalPath();
        rp.y = 0;
        refPoints.add(rp);
        lastRefPointTime = (double)TimeUtils.nanoTime();

    }

    // updates the current range of motion of the player if necessary
    public void updateRangeOfMotion(int xPos) {
        if (xPos < minXRange) {
            minXRange = xPos;
        } else if (xPos > maxXRange) {
            maxXRange = xPos;
        }
    }

    // writes the optimal and actual positions of the character as reference points pass the character
    private void writeToCSV(float optimal, float actual) {
        GameState.dataFile.write(optimal, actual);
    }

}
