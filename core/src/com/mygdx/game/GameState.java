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
    private int ONE_SEC_IN_MILLIS = 1000;
    private static int defaultStartingDurationInMin = 10;

    // physical game state components
    public Character character;
    public OptimalPath opt;
    public CoinPath cp;
    public ObstaclePath obstaclePath;
    public PowerPath powerPath;
    public Wind wind;
    public static DataFile dataFile;

    // render game components
    public SpriteBatch batch;
    public BitmapFont font;

    // internal game state
    public Long startTime = System.currentTimeMillis();
    public boolean isRunning = false;
    // default difficulty level
    public static int difficultySetting = 1;
    // default speed of scrolling coins
    public static float gameScrollSpeed = 75 + 25 * difficultySetting;
    // default duration of game
    public static int gameDurationSettingInSec = (int)(defaultStartingDurationInMin*60);
    // default range of motion velocity setting
    public static int rangeOfMotionSetting = 1;
    // display countdown set to not show
    public int displayCountdown = -1;


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
        obstaclePath = new ObstaclePath(GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT);
        wind = new Wind(GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT);

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
        obstaclePath.update(character);
        gameScrollSpeed += .001; // increment the game speed
        Scoreboard.getInstance().updateScoreboard();
        // end game if it exceeds the duration set in settings
        checkIfGameOver();
    }

    /**
     * Updates the position of the coin path and the optimal path
     */
    private void updateCoinPathsPos() {
        cp.updateCoinPath(character.charShape, character);
        opt.updateOptimalPath(character.charShape);
        opt.updateRangeOfMotion(character.getX());
        long timeGameHasBeenRunning = (System.currentTimeMillis() - startTime);
    }

    /**
     * Compares how long the game has been running to the duration of the game
     * set in settings and closes the game when the duration is exceeded.
     *
     * If within 10 seconds of ending, sets countdown to display remaining duration in seconds
     */
    public void checkIfGameOver() {
        // check if countdown should be displayed for last 10 seconds
        long timeGameHasBeenRunningInMillis = (System.currentTimeMillis() - startTime);
        long timeReaminingInMillis = gameDurationSettingInSec * 1000 - timeGameHasBeenRunningInMillis;
        if (timeReaminingInMillis <= 10 * ONE_SEC_IN_MILLIS) {
            // set countdown to display in seconds
            displayCountdown = (int)(timeReaminingInMillis / ONE_SEC_IN_MILLIS);

        }

        //Return to MainMenu Screen after duration of game play
        if (timeGameHasBeenRunningInMillis > gameDurationSettingInSec * ONE_SEC_IN_MILLIS){
            dataFile.close();
            this.setScreen(new GameEndScreen(this));
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

