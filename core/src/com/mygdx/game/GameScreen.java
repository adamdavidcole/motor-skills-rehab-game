package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;


public class GameScreen implements Screen {
    final MyGdxGame game;
    public Texture coinImage;
    public Texture characterImage;
    public Sound coinSound;
    public Music rainMusic;
    public OrthographicCamera camera;
    public Rectangle character;
    public Array<Rectangle> coins;
    public long lastCoinTime;
    public int coinsGathered;
    private int height = 800;
    private int width = 480;


    public GameScreen(final MyGdxGame gam) {
        this.game = gam;

        // load the images for the coin and the irishman, 64x64 pixels each
        coinImage = new Texture(Gdx.files.internal("coin.png"));
        characterImage = new Texture(Gdx.files.internal("bucket.png"));

        // load the coin sound effect and the rain background "music"
        //coinSound = Gdx.audio.newSound(Gdx.files.internal("coin.wav"));
        //rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
        //rainMusic.setLooping(true);

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, height, width);

        // create a Rectangle to logically represent the character
        character = new Rectangle();
        character.x = height / 2 - 64 / 2; // center the character horizontally
        character.y = width - character.getHeight() - 150; // bottom left corner of the character is 20 pixels above the bottom screen edge
        character.width = 64;
        character.height = 64;

        // create the coin array and spawn the first coin
        coins = new Array<Rectangle>();
        spawnCoin();

    }

    private void spawnCoin() {
        Rectangle coin = new Rectangle();
        coin.x = MathUtils.random(0, height-64);
        coin.y = 0;
        coin.width = 64;
        coin.height = 64;
        coins.add(coin);
        lastCoinTime = TimeUtils.nanoTime();
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

        // begin a new batch and draw the character and all coins
        game.batch.begin();
        game.font.draw(game.batch, "Coins Collected: " + coinsGathered, 0, width);
        game.batch.draw(characterImage, character.x, character.y);
        for (Rectangle c : coins) {
            game.batch.draw(coinImage, c.x, c.y);
        }
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
        if (character.x > height - 64)
            character.x = height - 64;

        // check if we need to create a new coin
        if (TimeUtils.nanoTime() - lastCoinTime > 1000000000)
            spawnCoin();

        // move the coins, remove any that are beneath the bottom edge of
        // the screen or that hit the character. In the latter case we increase the
        // value of our coins counter and add a sound effect.
        Iterator<Rectangle> iter = coins.iterator();
        while (iter.hasNext()) {
            Rectangle coin = iter.next();
            coin.y += 200 * Gdx.graphics.getDeltaTime();
            if (coin.y + 64 < 0)
                iter.remove();
            if (coin.overlaps(character)) {
                coinsGathered++;
                //coinSound.play();
                iter.remove();
            }
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
        coinImage.dispose();
        characterImage.dispose();
        //coinSound.dispose();
        //rainMusic.dispose();
    }

}

