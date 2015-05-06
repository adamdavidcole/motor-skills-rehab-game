package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.sql.Timestamp;

public class GameState extends com.badlogic.gdx.Game {

    public Character character;
    public OptimalPath opt;
    public CoinPath cp;
    private DataFile dataFile;



    public SpriteBatch batch;
    public BitmapFont font;

    private int height = 1280;
    private int width = 800;

    public int scrollVelocity = 200;
    public int remainingTimeSecs = 60;
    private Long startTime = System.currentTimeMillis();




    @Override
    public void create() {
        instantiateDataFile(); // instantiate dataFile
        // create a Rectangle to logically represent the charShape
        character = new Character(width, height);
        // create the coin path
        opt = new OptimalPath(width, height, dataFile);
        // create the optimal path
        cp = new CoinPath(width, height, opt);



        batch = new SpriteBatch();
        //Use LibGDX's default Arial font.
        font = new BitmapFont();
        this.setScreen(new LoginScreen(this));


    }

    private void instantiateDataFile() {
        String userTag = LoginScreen.username;
        String timestamp = new Timestamp(System.currentTimeMillis()).toString();
        String filename = (userTag + timestamp + ".csv").replace(":", "-").replace(" ","_");
        dataFile = new DataFile(filename);
        dataFile.writeHeader(LoginScreen.username, timestamp);
    }

    public void render() {
        super.render(); //important!
        update();
    }

    public void update() {
        updateCoinPathPos();
        // update character position and attributes
        character.update();


        //Return to MainMenu Screen after a minute of game play
        if (((System.currentTimeMillis() - startTime)/1000) > 30){
            dataFile.close();
            setScreen(new MainMenu(this));
        }
    }

    private void updateCoinPathPos() {
        // update the optimal path, coin path, and range of motion
        cp.updateCoinPath(character.charShape);
        opt.updateOptimalPath(character.charShape);
        opt.updateRangeOfMotion(character.getX());
    }



    public void dispose() {
        batch.dispose();
        font.dispose();
        cp.tearDown();
    }

}

