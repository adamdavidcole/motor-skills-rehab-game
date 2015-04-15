package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Iterator;


/*
 * Created by William Schiela on 4/14/15
 */
public class CoinPath {
    private Array<Rectangle> coins;
    private long lastCoinTime;

    // media files associated with a coin
    private Texture coinImage;
    private Sound coinSound;

    // height and width of screen on which CoinPath appears
    private int width;
    private int height;

    // sinusoidal path paramters
    private double PI = Math.PI;
    private int amplitude;
    private boolean inSecondHalfPeriod;
    private double period;
    private double t0;


    public CoinPath(int width, int height) {
        this.width = width;
        this.height = height;
        coins = new Array<Rectangle>();
        coinImage = new Texture(Gdx.files.internal("coin.png"));
        //coinSound = Gdx.audio.newSound(Gdx.files.internal("coin.wav"));

        amplitude = randomAmplitude();
        period = 4000000000.; // nanoseconds
        t0 = (double) TimeUtils.nanoTime();
        inSecondHalfPeriod = false;

        // spawn the first coin
        spawnCoin();

    }

    public void updateCoinPath(Rectangle character) {
        if (TimeUtils.nanoTime() - lastCoinTime > 500000000) {
            spawnCoin();
        }
        // move the coins, remove any that hit the character or are above the edge of the screen
        Iterator<Rectangle> iter = coins.iterator();
        while (iter.hasNext()) {
            Rectangle coin = iter.next();
            coin.y += 200 * Gdx.graphics.getDeltaTime();
            if (coin.y > height)
                iter.remove();
            if (coin.overlaps(character)) {
                Scoreboard sb = Scoreboard.getInstance();
                sb.addCoin();
                //coinSound.play();
                iter.remove();
            }
        }
    }

    private int randomAmplitude() {
        return MathUtils.random(0,(width-64)/2);
    }

    private float computeOptimalPath() {
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

    private void spawnCoin() {
        Rectangle coin = new Rectangle();
        coin.x = computeOptimalPath();
        coin.y = -64;
        coin.width = 64;
        coin.height = 64;
        coins.add(coin);
        lastCoinTime = TimeUtils.nanoTime();
    }

    public void renderCoinPath(SpriteBatch batch) {
        for (Rectangle c : coins) {
            batch.draw(coinImage, c.x, c.y);
        }
    }

    public void tearDown() {
        coinImage.dispose();
        // coinSound.dispose();
    }


}
