package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.TimeUtils;


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
//    private Background background;
    private Texture background;
    private float currentBgY;
    private long lastTimeBg;
    private Long startTime;
    private Music gameMusic;

    private DataFile dataFile;

    public static int SCROLL_VELOCITY = 200;


    public GameScreen(final MyGdxGame gam) {
        this.game = gam;

        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("gameSong.mp3"));
        gameMusic.setLooping(true);
        gameMusic.play();

        // load the image for the irishman, 64x64 pixels
//        characterImage = new Texture(Gdx.files.internal("bucket.png"));

        // load the rain background "music"
        //rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
        //rainMusic.setLooping(true);

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera(width, height);
        camera.setToOrtho(false, width, height);

        // create a Rectangle to logically represent the charShape
        character = new Character(width, height);

        // data file for exporting research data
        dataFile = new DataFile("dFile2.csv");

        // create the optimal path
        opt = new OptimalPath(width, height, dataFile);

        // create the coin path
        cp = new CoinPath(width, height, opt);

        // create the power path
        powerPath = new PowerPath(width, height);

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
        game.batch.end();

//
//        if (character.charShape.overlaps(pb.getRectangle())) {
//            character.powers.add(pb);
//            pb.dispose();
//        }


        // update the optimal path, coin path, and range of motion
        cp.updateCoinPath(character.charShape);
        opt.updateOptimalPath(character.charShape);
        opt.updateRangeOfMotion(character.getX());
        // update character position and attributes
        character.update();
        //Return to MainMenu Screen after a minute of game play
        if (((System.currentTimeMillis() - startTime)/1000) > 60){
            dataFile.close();
            game.setScreen(new MainMenu(game));
        }
        // pb.update();
        powerPath.update(character);

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

}

