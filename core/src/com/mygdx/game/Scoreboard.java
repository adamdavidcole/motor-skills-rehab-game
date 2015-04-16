package com.mygdx.game;

/**
 * Created by William Schiela on 4/14/2015.
 */
public class Scoreboard {
    private static Scoreboard instance;
    private int points;
    private int numCoins;
    private int multiplier;

    private final int POINTS_PER_COIN = 10;

    private Scoreboard() {
        points = 0;
        numCoins = 0;
        multiplier = 1;
    }

    public static Scoreboard getInstance() {
        if (instance == null) {
            instance = new Scoreboard();
        }
        return instance;
    }

    public void addCoin() {
        numCoins++;
        points += POINTS_PER_COIN*multiplier;
    }

    public void renderScoreboard(MyGdxGame game, int height) {
        game.font.draw(game.batch, "Coins Collected: " + numCoins, 0, height);
        game.font.draw(game.batch, "Points: " + points, 0, height-10);

    }
}