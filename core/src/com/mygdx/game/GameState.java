package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.sql.Timestamp;

/**
 * Holds all aspects and components of the game state such as the camera,
 * characters, coins, the clock, etc.
 */
public class GameState extends com.badlogic.gdx.Game {
    // camera and screen dimensions
    public static OrthographicCamera camera;
    public static int GAME_WORLD_WIDTH;
    public static int GAME_WORLD_HEIGHT;

    // physical game state components
    public Character character;
    public OptimalPath opt;
    public CoinPath cp;
    public PowerPath powerPath;
    public static DataFile dataFile;

    // render game components
    public SpriteBatch batch;
    public BitmapFont font;

    // internal game state
    public Long startTime = System.currentTimeMillis();
    public boolean isRunning = false;
    public static int difficultySetting = 1;
    public static float gameScrollSpeed = 75 + 25 * difficultySetting;
    public static int gameDurationSetting = 15*60;
    public static int rangeOfMotionSetting = 1;


    /**
     * Creates an instance of the game by instantiating the camera based on screen
     * coordinates, the separate game components, and sets the first screen.
     */
    @Override
    public void create() {
        // set up game camera to use for all screens
        float w = Gdx.graphics.getWidth();  // screen width
        float h = Gdx.graphics.getHeight(); // screen height
        GAME_WORLD_WIDTH = 800;             // world width
        GAME_WORLD_HEIGHT  = 850;           // world height
        float aspectRatio =  w / h;         // aspect ratio of screen

        // create a new orthographic camera for the world and center it
        camera = new OrthographicCamera(GAME_WORLD_HEIGHT * aspectRatio, GAME_WORLD_HEIGHT);
        camera.position.set(GAME_WORLD_WIDTH/2f,GAME_WORLD_HEIGHT/2f,0);

        // instantiate the physical components of the game
        character = new Character(GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT);
        opt = new OptimalPath(GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT, dataFile);
        cp = new CoinPath(GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT, opt);
        powerPath = new PowerPath(GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT);


        // instantiate the render components of the game
        batch = new SpriteBatch();
        font = new BitmapFont();

        // upon creation of the game, set screen to login screen
        this.setScreen(new LoginScreen(this));
    }

    /**
     * Changes the state of the game to running
     */
    public void startGame() {
        isRunning = true;
    }

    /**
     * Changes the state of the game to not running
     */
    public void stopGame() {
        isRunning = false;
    }

    /**
     * Instantiates the output data file with the name from provided by the user at login
     */
    public void instantiateDataFile() {
        String userTag = LoginScreen.username;
        String timestamp = new Timestamp(System.currentTimeMillis()).toString();
        String filename = (userTag + timestamp + ".csv").replace(":", "-").replace(" ","_");
        dataFile = new DataFile(filename);
        dataFile.writeHeader(LoginScreen.username, timestamp);
    }

    /**
     * Loop called every 1/60 of a second to update the game state. Only updates when game is
     * running.
     */
    public void render() {
        camera.update();
        super.render(); //important!
        if (isRunning) update();
    }

    /**
     * Updates the state of the game.
     */
    public void update() {
        updateCoinPathsPos();   // update coin paths
        character.update();     // update state of character
        powerPath.update(character); // update power path
        gameScrollSpeed += .001; // increment the game speed

        // end game if it exceeds the duration set in settings
        checkIfGameOver();
    }

    /**
     * Updates the position of the coin path and the optimal path
     */
    private void updateCoinPathsPos() {
        cp.updateCoinPath(character.charShape);
        opt.updateOptimalPath(character.charShape);
        opt.updateRangeOfMotion(character.getX());
        long timeGameHasBeenRunning = (System.currentTimeMillis() - startTime);
    }

    /**
     * Compares how long the game has been running to the duration of the game
     * set in settings and closes the game when the duration is exceeded.
     */
    public void checkIfGameOver() {
        //Return to MainMenu Screen after a minutes of game play
        long timeGameHasBeenRunning = (System.currentTimeMillis() - startTime);
        if (timeGameHasBeenRunning > gameDurationSetting * 1000 * 60){
            dataFile.close();
            this.setScreen(new MainMenu(this));
        }
    }


    /**
     * Pauses the state of the game
     */
    @Override
    public void pause() {
        isRunning = false;
    }

    /**
     * Releases memory on destruction
     */
    public void dispose() {
        batch.dispose();
        font.dispose();
        cp.tearDown();
    }

}

