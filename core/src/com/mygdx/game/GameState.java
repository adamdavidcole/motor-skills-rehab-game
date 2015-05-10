package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.sql.Timestamp;

public class GameState extends com.badlogic.gdx.Game {
    public static OrthographicCamera camera;
    public static int GAME_WORLD_WIDTH;
    public static int GAME_WORLD_HEIGHT;
//    private float aspectRatio = (float) Gdx.graphics.getWidth() / (float) Gdx.graphics.getHeight();


    public Character character;
    public OptimalPath opt;
    public CoinPath cp;
    public PowerPath powerPath;
    public static DataFile dataFile;



    public SpriteBatch batch;
    public BitmapFont font;

    private int height = 1280;
    private int width = 800;

    public int scrollVelocity = 200;
    public int remainingTimeSecs = 60;

    public Long startTime = System.currentTimeMillis();

    public boolean isRunning = false;
    public static float gameScrollSpeed = 75 + 25 * Settings.getInstance().difficulty;
    public static int difficulty = 1;


    @Override
    public void create() {
        System.out.println(Gdx.graphics.getWidth());
        System.out.println(Gdx.graphics.getHeight());
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        GAME_WORLD_WIDTH = 800;
        GAME_WORLD_HEIGHT  = 850;
        float aspectRatio =  w / h;

        camera = new OrthographicCamera(GAME_WORLD_HEIGHT * aspectRatio, GAME_WORLD_HEIGHT);
        camera.position.set(GAME_WORLD_WIDTH/2f,GAME_WORLD_HEIGHT/2f,0);
        // instantiate the dataFile
        // create a Rectangle to logically represent the charShape
        character = new Character(GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT);
        // create the coin path
        opt = new OptimalPath(GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT, dataFile);
        // create the optimal path
        cp = new CoinPath(GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT, opt);
        // create the power path
        powerPath = new PowerPath(GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT);



        batch = new SpriteBatch();
        //Use LibGDX's default Arial font.
        font = new BitmapFont();
        this.setScreen(new LoginScreen(this));
    }

    public void startGame() {
        isRunning = true;
    }

    public void stopGame() {
        isRunning = false;
    }

    public void instantiateDataFile() {
        String userTag = LoginScreen.username;
        String timestamp = new Timestamp(System.currentTimeMillis()).toString();
        String filename = (userTag + timestamp + ".csv").replace(":", "-").replace(" ","_");
        dataFile = new DataFile(filename);
        dataFile.writeHeader(LoginScreen.username, timestamp);
    }

    public void render() {
        camera.update();
        super.render(); //important!
        if (isRunning) update();
    }

    public void update() {
        updateCoinPathPos();
        // update character position and attributes
        character.update();
        powerPath.update(character);


        checkIfGameOver();
    }

    private void updateCoinPathPos() {
        // update the optimal path, coin path, and range of motion
        cp.updateCoinPath(character.charShape);
        opt.updateOptimalPath(character.charShape);
        opt.updateRangeOfMotion(character.getX());
        long timeGameHasBeenRunning = (System.currentTimeMillis() - startTime);
        gameScrollSpeed += .001;
    }

    public void checkIfGameOver() {
        //Return to MainMenu Screen after a minutes of game play
        long timeGameHasBeenRunning = (System.currentTimeMillis() - startTime);
        if (timeGameHasBeenRunning > Settings.getInstance().gameDuration * 1000 * 60){
            dataFile.close();
            this.setScreen(new MainMenu(this));
        }
    }


    @Override
    public void pause() {
        isRunning = false;
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
        cp.tearDown();
    }

}

