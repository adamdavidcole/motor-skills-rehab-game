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


public class GameScreen implements Screen {
    final MyGdxGame game;
//    private Texture characterImage;
    private OrthographicCamera camera;
    private OptimalPath opt;
    //private Rectangle charShape;
    private int height = 1280;
    private int width = 800;
    private Character character;
    private CoinPath cp;
//    private PoisonBottle pb;
    private PowerPath powerPath;
    private ObstaclePath obstaclePath;
//    private Background background;
    private Texture background;
    private float currentBgY;
    private long lastTimeBg;
    private Long startTime;
    private Music gameMusic;
    private Boolean windActive = false;
    private Boolean windBlowing = false;
    private int windDuration = 0;
    private Texture wAlert = new Texture(Gdx.files.internal("windAlert.png"));
    private Wind wind;

    private DataFile dataFile;

    public static int SCROLL_VELOCITY = 200;
    public int GAME_TIMER = 60;
    private Stage stage;


    public GameScreen(final MyGdxGame gam) {
        this.game = gam;

        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("gameSong.mp3"));
        gameMusic.setLooping(true);
        gameMusic.setVolume(0.5f); // to allow sound effects to be heard
        gameMusic.play();

        // load the image for the irishman, 64x64 pixels
        // characterImage = new Texture(Gdx.files.internal("bucket.png"));

        // load the rain background "music"
        //rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
        //rainMusic.setLooping(true);

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera(width, height);
        camera.setToOrtho(false, width, height);

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

        // create a Rectangle to logically represent the charShape
        character = new Character(width, height);

        // data file for exporting research data
        String userTag = LoginScreen.username;
        String timestamp = new Timestamp(System.currentTimeMillis()).toString();
        String filename = (userTag + timestamp + ".csv").replace(":", "-").replace(" ","_");
        dataFile = new DataFile(filename);
        dataFile.writeHeader(LoginScreen.username, timestamp);

        // create the optimal path
        opt = new OptimalPath(width, height, dataFile);

        // create the coin path
        cp = new CoinPath(width, height, opt);

        // create the power path
        powerPath = new PowerPath(width, height);

        // create obstacle path
        obstaclePath = new ObstaclePath(width, height);

        // create the wind object
        wind = new Wind(width, height);

        //create the background
//        background = new Background(width, height);
        //note time when application starts
        startTime = System.currentTimeMillis();

//      pb = new PoisonBottle();
        background = new Texture(Gdx.files.internal("cloudBGSmall.png"));
// the separator first appear at the position 800 (the edge of the screen, see
// the camera above)
        currentBgY = height;
        // set lastTimeBg to current time
        lastTimeBg = TimeUtils.nanoTime();

        

    }

    @Override
    public void render(float delta) {
        // clear the screen with a dark blue color. The
        // arguments to glClearColor are the red, green
        // blue and alpha component in the range [0,1]
        // of the color to be used to clear the screen.
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);

        // begin a new batch and draw the charShape and all coins, and scoreboard
        game.batch.begin();
        game.batch.draw(background, 0, currentBgY - height);
        game.batch.draw(background, 0, currentBgY);

        Scoreboard sb = Scoreboard.getInstance();
        sb.renderScoreboard(game, height);
        character.render(game.batch);
        cp.renderCoinPath(game.batch);
//        pb.render(game.batch);
        powerPath.render(game.batch);
        obstaclePath.render(game.batch);

        ////////////////////////////////////////////////////////////////////////
        //// WIND GRAPHICS - can't be done on thread or separately due to LibGDX
        if (windActive) {
            if (windBlowing) {
                wind.render(game.batch);
                // character is blown by the wind
                character.setX((int)(character.getX() - 50 * Gdx.graphics.getDeltaTime()));
            } else { // wind not blowing, but active - 3 options: alerting, entering, exiting
                if (wind.exiting()){ // if exiting (the check updates the position)
                    wind.render(game.batch);
                } else if (wind.entering()){ // if entering (the check updates the position)
                    wind.render(game.batch);
                } else { // wind is alerting - about to enter
                    game.batch.draw(wAlert, width - wAlert.getWidth() - 50, height - wAlert.getHeight() - 80,
                            wAlert.getWidth(), wAlert.getHeight());
                }
            }
        }

        game.batch.end();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        stage.setDebugAll(true);

//
//        if (character.charShape.overlaps(pb.getRectangle())) {
//            character.powers.add(pb);
//            pb.dispose();
//        }


        // update the optimal path, coin path, and range of motion
        cp.updateCoinPath(character.charShape, character);
        opt.updateOptimalPath(character.charShape);
        opt.updateRangeOfMotion(character.getX());
        // update character position and attributes
        character.update();
        //Return to MainMenu Screen after a minute of game play
        if (((System.currentTimeMillis() - startTime)/1000) > GAME_TIMER){
            dataFile.close();
            game.setScreen(new MainMenu(game));
        }
        // pb.update();
        powerPath.update(character);
        obstaclePath.update(character);

        // move the separator each 1s
        if(TimeUtils.nanoTime() - lastTimeBg > 10000000){
            // move the separator 50px
            currentBgY += 1;
            // set the current time to lastTimeBg
            lastTimeBg = TimeUtils.nanoTime();
        }

        ///////////////////////////////////////////////////////////
        ///////////////// WIND SEGMENT
        // Note: must be done in main render loop because of how libGDX works
        if (!windActive) {
            // .04% chance of spawning with enforced wait time in between spawns
            windDuration = wind.update();
            if (windDuration != 0) {
                windActive = true;
                System.out.println("Wind duration is "+windDuration);

                final Sound windAlertBeep = Gdx.audio.newSound(Gdx.files.internal("windAlertBeep.wav"));

                //create a timer to loop for 4s
                final long id = windAlertBeep.loop();
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        windAlertBeep.stop(id);
                        windAlertBeep.dispose();
                        // COMMENCE CLOUD COMING OUT NOW
                        wind.enter();
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

// if the separator reaches the screen edge, move it back to the first position
        if(currentBgY > height){
            currentBgY = 0;
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        // start the playback of the background music
        // when the screen is shown
        // rainMusic.play();
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        //characterImage.dispose();
        //rainMusic.dispose();
        cp.tearDown();
    }

    private void setWind(boolean blowing) {
        System.out.println("setWind called: "+blowing);
        if (blowing) {
            wind.playSound();

            // set timer for wind duration
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    System.out.println("wind exiting");
                    wind.windExit();
                    setWind(false);
                }
            }, ((float)windDuration/1000.0f)); // 1000 milli seconds in a second
        } else {
            wind.stopSound();
            // set a timer to dispose once done exiting
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    windActive = false;
                    wind.doneExiting();
                }
            }, 4.0f); // after 4 seconds
        }
        windBlowing = blowing;
    }

}

