package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;

import java.sql.Timestamp;
import com.badlogic.gdx.utils.viewport.FitViewport;


/**
 * GameScreen class renders the actual gameplay including the character, coins, and powerups
 */
public class GameScreen implements Screen {

    // Universal game state
    final GameState game;

    // background image and positions
    private Texture background;
    private float currentBgY;
    private long lastTimeBg;

    // components of main game screen
    private Soundtrack soundtrack;
    private Stage stage;


    private long ONE_SEC_IN_NANO = 10000000;
    /**
     * Game screen constructor initializes game stage, soundtrack, and moving background
     * @param gam
     */
    public GameScreen(final GameState gam) {
        this.game = gam;
        stage = new Stage();

        Gdx.input.setInputProcessor(stage);
        soundtrack = new Soundtrack();

        // creates the button to go back to the main menu
        generateBackButton();

        // instantiates background
        background = new Texture(Gdx.files.internal("cloudBGSmall.png"));
        // current position of background
        currentBgY = game.GAME_WORLD_HEIGHT;
        // last time background looped
        lastTimeBg = TimeUtils.nanoTime();
    }

    /**
     * Renders the moving background, scoreboard, character, powers, and coins
     * @param delta
     */
    @Override
    public void render(float delta) {
        // clear the screen with a dark blue color.
        Gdx.gl.glClearColor(.005f, .006f, .121f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell the camera to update its matrices.
        game.camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(game.camera.combined);

        // begin a new batch and draw the character, all coins, and scoreboard
        game.batch.begin();
        game.batch.draw(background, 0, currentBgY - game.GAME_WORLD_HEIGHT);
        game.batch.draw(background, 0, currentBgY);
        Scoreboard sb = Scoreboard.getInstance();

        sb.renderScoreboard(game, game.GAME_WORLD_HEIGHT);
        game.character.render(game.batch);
        game.cp.renderCoinPath(game.batch);
        game.powerPath.render(game.batch);
        game.obstaclePath.render(game.batch);

        ////////////////////////////////////////////////////////////////////////
        //// WIND GRAPHICS - can't be done on thread or separately due to LibGDX
        if (game.wind.windActive) {
            if (game.wind.windBlowing) {
                game.wind.render(game.batch);
                // character is blown by the wind
                game.character.shiftCharacter(-.3);
            } else { // wind not blowing, but active - 3 options: alerting, entering, exiting
                if (game.wind.exiting()){ // if exiting (the check updates the position)
                   game.wind.render(game.batch);
                } else if (game.wind.entering()){ // if entering (the check updates the position)
                    game.wind.render(game.batch);
                } else { // wind is alerting - about to enter
                    game.batch.draw(game.wind.wAlert, game.GAME_WORLD_WIDTH - game.wind.wAlert.getWidth() - 50, game.GAME_WORLD_HEIGHT - game.wind.wAlert.getHeight() - 80,
                            game.wind.wAlert.getWidth(), game.wind.wAlert.getHeight());
                }
            }
        }

        game.batch.end();


        // draw the back button
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        stage.setDebugAll(false);




        // move the moving background separator each second
        if(TimeUtils.nanoTime() - lastTimeBg > ONE_SEC_IN_NANO){
            // move the separator 1 unit
            currentBgY += 1;
            // set the current time to lastTimeBg
            lastTimeBg = TimeUtils.nanoTime();
        }

        ///////////////////////////////////////////////////////////
        ///////////////// WIND SEGMENT
        // Note: must be done in main render loop because of how libGDX works
        if (!game.wind.windActive) {
            // .04% chance of spawning with enforced wait time in between spawns
            game.wind.windDuration = game.wind.update();
            if (game.wind.windDuration != 0) {
                game.wind.windActive = true;
                System.out.println("Wind duration is "+game.wind.windDuration);

                final Sound windAlertBeep = Gdx.audio.newSound(Gdx.files.internal("windAlertBeep.wav"));

                //create a timer to loop for 4s
                final long id = windAlertBeep.loop();
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        windAlertBeep.stop(id);
                        windAlertBeep.dispose();
                        // COMMENCE CLOUD COMING OUT NOW
                        game.wind.enter();
                    }
                }, 4.0f); // run for 4 seconds

                //create a timer to start wind in 7s (once wind is done entering)
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        setWind(true);
                    }
                }, 7.0f); // run for 7 seconds

            }
        }


       // if the background seprator reaches the screen edge, move it back to the first position
        if(currentBgY > game.GAME_WORLD_HEIGHT){
            currentBgY = 0;
        }
    }


    @Override
    public void resize(int width, int height) {
    }


    /**
     * Begin game, music, and game clock when this screen is shown
     */
    @Override
    public void show() {
        game.startGame();
        game.startTime = System.currentTimeMillis();
        soundtrack.gameMusic.play();
    }

    /**
     * Pause the game and music when this screen is hidden
     */
    @Override
    public void hide() {
        game.stopGame();
        soundtrack.gameMusic.stop();
    }

    @Override
    public void pause() {
    }

    /**
     * Resume the game when the screen resumed
     */
    @Override
    public void resume() {
        game.startGame();
    }

    @Override
    public void dispose() {
    }

    /**
     * Genrates the back button that returns user to main menu screen
     */
    private void generateBackButton () {
        //create the stage for buttons
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        //create button table to hold textbuttons: play, settings and quit
        Table buttonTable = new Table();
        buttonTable.setFillParent(true);

        //create button style
        Texture buttonTexture = new Texture(Gdx.files.internal("MainMenuButton.png"));
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = game.font;
        buttonStyle.up = new TextureRegionDrawable(new TextureRegion(buttonTexture));

        //create quit button
        TextButton backButton = new TextButton("BACK", buttonStyle);
        buttonTable.add(backButton);
        stage.addActor(buttonTable);

        //add a listener for the quit button
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                //back to main menu of the app
                game.setScreen(new MainMenu(game));
            }
        });
    }

    private void setWind(boolean blowing) {
        System.out.println("setWind called: "+blowing);
        if (blowing) {
            game.wind.playSound();

            // set timer for wind duration
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    System.out.println("wind exiting");
                    game.wind.windExit();
                    setWind(false);
                }
            }, ((float)game.wind.windDuration/1000.0f)); // 1000 milli seconds in a second
        } else {
            game.wind.stopSound();
            // set a timer to dispose once done exiting
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    game.wind.windActive = false;
                    game.wind.doneExiting();
                }
            }, 9.0f); // after 4 seconds
        }
        game.wind.windBlowing = blowing;
    }

}

