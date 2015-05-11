package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import javafx.stage.Stage;

/**
 * A class to track game player data such as number of coins, points, and point multipliers
 */
public class Scoreboard {
    private static Scoreboard instance;
    private BitmapFont font;
    private int points;
    private int numCoins;
    private float multiplier;
    private float SB_LINE_HEIGHT = 30;
    private float SB_HORIZONTAL_POS = 0;
    private float SB_VERTICAL_POS = 0;
    Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
    Label coinsCollectedLabel = new Label("", skin);
    Label pointsLabel = new Label("", skin);
    Label multiplierLabel = new Label("", skin);





    private final int POINTS_PER_COIN = 10;

    private Scoreboard() {
        points = 0;
        numCoins = 0;
        multiplier = 1;
        font = new BitmapFont();
        font.setColor(new Color(Color.BLACK));
        font.setScale(1.5f);

        coinsCollectedLabel.setFontScale(1.5f);
        pointsLabel.setFontScale(1.5f);
        multiplierLabel.setFontScale(1.5f);

        float leftGutter = (Gdx.graphics.getWidth() - GameState.GAME_WORLD_WIDTH) / 2f;
        float verticalPos = GameState.GAME_WORLD_HEIGHT - SB_VERTICAL_POS;
        coinsCollectedLabel.setPosition(leftGutter + SB_HORIZONTAL_POS, verticalPos);
        pointsLabel.setPosition(leftGutter + SB_HORIZONTAL_POS, verticalPos-SB_LINE_HEIGHT);
        multiplierLabel.setPosition(leftGutter + SB_HORIZONTAL_POS, verticalPos - 2 * SB_LINE_HEIGHT);
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
        points += POINTS_PER_COIN*multiplier*GameState.difficultySetting;
    }

    public void addPot() {
        numCoins += 50;
        points += 50*POINTS_PER_COIN*multiplier;
    }

    // draws the scoreboard on the game screen
  /*  public void renderScoreboard(GameState game, int height) {
        float verticalPos = height - SB_VERTICAL_POS;
        font.draw(game.batch, "Coins Collected: " + numCoins, SB_HORIZONTAL_POS, verticalPos);
        font.draw(game.batch, "Points: " + points, SB_HORIZONTAL_POS, verticalPos-SB_LINE_HEIGHT);
        font.draw(game.batch, "Multiplier = X" + multiplier, SB_HORIZONTAL_POS,
                verticalPos - 2 * SB_LINE_HEIGHT);

    }*/
    public void renderScoreboard() {
        coinsCollectedLabel.setText("Coins Collected: " + numCoins);
        pointsLabel.setText("Points: " + points);
        multiplierLabel.setText("Multiplier = X" + multiplier);
    }

    /**
     * Scoreboard rendered in the final game over screen
     */
    public void renderGameOverScoreboard(GameState game, int height) {
        float verticalOffset = - 100;
        font.draw(game.batch, "Coins Collected: " + numCoins, game.GAME_WORLD_WIDTH / 2 + verticalOffset, game.GAME_WORLD_HEIGHT / 2);
        font.draw(game.batch, "Points: " + points,  game.GAME_WORLD_WIDTH / 2 + verticalOffset, game.GAME_WORLD_HEIGHT / 2 - SB_LINE_HEIGHT);
        //font.draw(game.batch, "Multiplier = X" + multiplier,  game.GAME_WORLD_WIDTH / 2 - 100, game.GAME_WORLD_HEIGHT / 2 - 2 * SB_LINE_HEIGHT);
    }

    public void setMultiplier(float mult) {
        multiplier = mult;
    }

    public float getMultiplier() { return multiplier; }
 }