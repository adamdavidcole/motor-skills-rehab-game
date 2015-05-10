package com.mygdx.game;

/**
 * A class to track game player data such as number of coins, points, and point multipliers
 */
public class Scoreboard {
    private static Scoreboard instance;
    private int points;
    private int numCoins;
    private float multiplier;

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

    // updates the number of coins collected and the number of points
    public void addCoin() {
        numCoins++;
        points += POINTS_PER_COIN*multiplier;
    }

    // draws the scoreboard on the screen
    public void renderScoreboard(GameState game, int height) {
        game.font.draw(game.batch, "Coins Collected: " + numCoins, 0, height);
        game.font.draw(game.batch, "Points: " + points, 0, height-15);
        game.font.draw(game.batch, "Multiplier = X" + multiplier, 0, height - 30);

    }

    public void setMultiplier(float mult) {
        multiplier = mult;
    }

    public float getMultiplier() { return multiplier; }
 }