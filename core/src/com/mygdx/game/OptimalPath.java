package com.mygdx.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Created by William Schiela on 4/15/2015.
 */
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

    public OptimalPath(int width, int height) {
        this.width = width;
        this.height = height;

        amplitude = randomAmplitude();
        period = 4000000000.; // nanoseconds
        t0 = (double) TimeUtils.nanoTime();
        inSecondHalfPeriod = false;

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

}
