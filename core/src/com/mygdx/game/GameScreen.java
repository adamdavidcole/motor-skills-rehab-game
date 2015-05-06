package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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


public class GameScreen implements Screen {
    final GameState game;
//    private Texture characterImage;
    private OrthographicCamera camera;
    //private Rectangle charShape;
    public static int SCROLL_VELOCITY = 100;

//    private Background background;
    private Texture background;
    private float currentBgY;
    private long lastTimeBg;

    //private Music gameMusic;
    private Soundtrack soundtrack;
    private Stage stage;

    private int height = 1280;
    private int width = 800;


    public GameScreen(final GameState gam) {
        this.game = gam;
        stage = new Stage();

        Gdx.input.setInputProcessor(stage);
        soundtrack = new Soundtrack();
        //gameMusic = Gdx.audio.newMusic(Gdx.files.internal("gameSong.mp3"));
        //gameMusic.setLooping(false);


        // load the image for the irishman, 64x64 pixels
        // characterImage = new Texture(Gdx.files.internal("bucket.png"));

        // load the rain background "music"
        //rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
        //rainMusic.setLooping(true);

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera(width, height);
        camera.setToOrtho(false, width, height);


        // creates the button to go back to the main menu
        generateBackButton();



        //create the background
//        background = new Background(width, height);
        //note time when application starts

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
        game.character.render(game.batch);
        game.cp.renderCoinPath(game.batch);
        game.powerPath.render(game.batch);
        game.batch.end();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        stage.setDebugAll(true);

//
//        if (character.charShape.overlaps(pb.getRectangle())) {
//            character.powers.add(pb);
//            pb.dispose();
//        }


//
//        //Return to MainMenu Screen after a minute of game play
//        if (((System.currentTimeMillis() - startTime)/1000) > 60){
//            dataFile.close();
//            game.setScreen(new MainMenu(game));
//        }
        // pb.update();

        // move the separator each 1s
        if(TimeUtils.nanoTime() - lastTimeBg > 10000000){
            // move the separator 50px
            currentBgY += 1;
            // set the current time to lastTimeBg
            lastTimeBg = TimeUtils.nanoTime();
        }

// if the seprator reaches the screen edge, move it back to the first position
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
        game.startGame();
        game.startTime = System.currentTimeMillis();
        soundtrack.gameMusic.play();
    }

    @Override
    public void hide() {
        game.stopGame();
        soundtrack.gameMusic.stop();
    }

    @Override
    public void pause() {
//        game.stopGame();
//        soundtrack.gameMusic.stop();

    }

    @Override
    public void resume() {
        game.startGame();
//        game.startTime = System.currentTimeMillis();
//
//        soundtrack.gameMusic.play();
    }

    @Override
    public void dispose() {
        //characterImage.dispose();
        //rainMusic.dispose();
//        cp.tearDown();
    }

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

}

