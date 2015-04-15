package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;


public class GameScreen implements Screen {
    final MyGdxGame game;
    private Texture characterImage;
    private Music rainMusic;
    private OrthographicCamera camera;
    private Rectangle character;
    private final int height = 1280;
    private final int width = 800;
    private CoinPath cp;


    public GameScreen(final MyGdxGame gam) {
        this.game = gam;

        // load the image for the irishman, 64x64 pixels
        characterImage = new Texture(Gdx.files.internal("bucket.png"));

        // load the rain background "music"
        //rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
        //rainMusic.setLooping(true);

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, width, height);

        // create a Rectangle to logically represent the character
        character = new Rectangle();
        character.x = height / 2 - 64 / 2; // center the character horizontally
        character.y = height - character.getHeight() - 150; // bottom left corner of the character is 20 pixels above the bottom screen edge
        character.width = 64;
        character.height = 64;

        // create the optimal path
        OptimalPath opt = new OptimalPath(width, height);

        // create the coin path
        cp = new CoinPath(width, height, opt);
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

        // begin a new batch and draw the character and all coins, and scoreboard
        game.batch.begin();
        game.batch.draw(characterImage, character.x, character.y);
        Scoreboard sb = Scoreboard.getInstance();
        sb.renderScoreboard(game, height);
        cp.renderCoinPath(game.batch);
        game.batch.end();

        // process user input
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            character.x = touchPos.x - 64 / 2;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            character.x -= 200 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            character.x += 200 * Gdx.graphics.getDeltaTime();

        // make sure the character stays within the screen bounds
        if (character.x < 0)
            character.x = 0;
        if (character.x > width - 64)
            character.x = width - 64;

        // update the coin path
        cp.updateCoinPath(character);
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
        characterImage.dispose();
        //rainMusic.dispose();
        cp.tearDown();
    }

}

