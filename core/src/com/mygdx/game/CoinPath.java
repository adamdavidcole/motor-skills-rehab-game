package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Iterator;


/*
 * Created by William Schiela on 4/14/15
 */
// a class to handle the spawning of coins and all other coin-related functionality
public class CoinPath {
    private Array<Rectangle> coins;
    private double lastCoinTime;
    private final double SPAWN_INTERVAL = 500000000.;

    // media files associated with a coin
    private Texture coinImage;
    private Sound coinSound;

    // height and width of screen on which CoinPath appears
    private int width;
    private int height;

    // optimal path on which coins appear
    OptimalPath opt;

    public CoinPath(int width, int height, OptimalPath opt) {
        this.width = width;
        this.height = height;
        coins = new Array<Rectangle>();
        coinImage = new Texture(Gdx.files.internal("coin.png"));
        //coinSound = Gdx.audio.newSound(Gdx.files.internal("coin.wav"));

        this.opt = opt;

        coinSound = Gdx.audio.newSound(Gdx.files.internal("coinCollectSound2.wav"));

        // spawn the first coin
        spawnCoin();

    }

    // moves the coins up on the screen and spawns a coin every SPAWN_INTERVAL
    // checks for coins collected by the character and removes them, adding the points to the scoreboard
    public void updateCoinPath(Rectangle charShape) {
        if ((double)TimeUtils.nanoTime() - lastCoinTime > SPAWN_INTERVAL) {
            spawnCoin();
        }
        // move the coins, remove any that hit the charShape or are above the edge of the screen
        Iterator<Rectangle> iter = coins.iterator();
        while (iter.hasNext()) {
            Rectangle coin = iter.next();
            coin.y += GameScreen.SCROLL_VELOCITY * Gdx.graphics.getDeltaTime();
            if (coin.y > height)
                iter.remove();
            if (coin.overlaps(charShape)) {
                Scoreboard sb = Scoreboard.getInstance();
                sb.addCoin();
                coinSound.play();
                //coinSound.play();
                iter.remove();
            }
        }
    }

    // spawns a coin along the optimal path
    private void spawnCoin() {
        Rectangle coin = new Rectangle();
        coin.x = opt.computeOptimalPath();
        coin.y = -64;
        coin.width = 64;
        coin.height = 64;
        coins.add(coin);
        lastCoinTime = (double) TimeUtils.nanoTime();
    }

    // draws the coin path on the screen
    public void renderCoinPath(SpriteBatch batch) {
        for (Rectangle c : coins) {
            batch.draw(coinImage, c.x, c.y, c.width, c.height);
        }
    }

    // releases all resources associated with the coins.
    public void tearDown() {
        coinImage.dispose();
        coinSound.dispose();
    }

}
