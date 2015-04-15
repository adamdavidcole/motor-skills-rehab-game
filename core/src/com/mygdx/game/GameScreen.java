package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;


public class GameScreen implements Screen {
    final MyGdxGame game;
//    private Texture characterImage;
    private Music rainMusic;
    private OrthographicCamera camera;
    //private Rectangle charShape;
    private int height = 1280;
    private int width = 800;
    private Character character;
    private CoinPath cp;
//    private PoisonBottle pb;
    private PowerPath powerPath;



    public GameScreen(final MyGdxGame gam) {
        this.game = gam;

        // load the image for the irishman, 64x64 pixels
//        characterImage = new Texture(Gdx.files.internal("bucket.png"));

        // load the rain background "music"
        //rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
        //rainMusic.setLooping(true);

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, width, height);

        // create a Rectangle to logically represent the charShape
        character = new Character(width, height);

        // create the coin path
        cp = new CoinPath(width, height);

        // create the power path
        powerPath = new PowerPath(width, height);

//        pb = new PoisonBottle();
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

        // begin a new batch and draw the charShape and all coins
        game.batch.begin();
        game.font.draw(game.batch, "Coins Collected: " + "IMPLEMENT SCOREBOARD", 0, height);
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


        // update character position and attributes
        character.update();
        // update the coin path
        cp.updateCoinPath(character.charShape);
//        pb.update();
        powerPath.update(character);
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

