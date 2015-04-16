package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.awt.Point;
import java.util.Iterator;

/**
 * Created by William Schiela on 4/15/2015.
 */

// tracks research data such as distance from the optimal path and users maximum range of motion
public class OptimalPath {
    // width and height of the screen on which the path exists
    private int width;
    private int height;

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

    private float maxXRange;
    private float minXRange;

    public OptimalPath(int width, int height) {
        this.width = width;
        this.height = height;

        refPoints = new Array<Point>();

        amplitude = randomAmplitude();
        period = 4000000000.; // nanoseconds
        t0 = (double) TimeUtils.nanoTime();
        inSecondHalfPeriod = false;

        maxXRange = (width - 64) / 2;
        minXRange = (width - 64) / 2;

        // spawn the first reference point
        spawnRefPoint();
    }

    private int randomAmplitude() {
        return MathUtils.random(0, (width - 64) / 2);
    }

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
        double offset = (width - 64) / 2;
        double argument = 2*PI*t/period;

        float xPosition = (float) (offset + amplitude*Math.sin(argument));
        return xPosition;
    }

    public void updateOptimalPath(Rectangle character) {
        if (TimeUtils.nanoTime() - lastRefPointTime > SAMPLE_INTERVAL) {
            spawnRefPoint();
        }
        // move the reference points, removing them as they pass the character while ex
        Iterator<Point> iter = refPoints.iterator();
        while (iter.hasNext()) {
            Point rp = iter.next();
            rp.y += GameScreen.SCROLL_VELOCITY * Gdx.graphics.getDeltaTime();
            if (rp.y > height)
                iter.remove();
            if (rp.y >= character.y) {
                writeToCSV(rp.x, character.x);
                iter.remove();
            }
        }
    }

    private void spawnRefPoint() {
        Point rp = new Point();
        rp.x = (int) computeOptimalPath();
        rp.y = -64;
        refPoints.add(rp);
        lastRefPointTime = (double)TimeUtils.nanoTime();

    }

    public void updateRangeOfMotion(float xPos) {
        if (xPos < minXRange) {
            minXRange = xPos;
        } else if (xPos > maxXRange) {
            maxXRange = xPos;
        }
    }

    private void writeToCSV(float optimal, float actual) {
        // TODO: implement
        System.out.println(optimal + "\t" + actual);
    }

}
