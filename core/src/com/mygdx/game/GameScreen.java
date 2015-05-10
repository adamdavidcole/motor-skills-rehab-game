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
import com.badlogic.gdx.utils.viewport.FitViewport;


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


    public GameScreen(final GameState gam) {
        this.game = gam;
        stage = new Stage();

        Gdx.input.setInputProcessor(stage);
        soundtrack = new Soundtrack();


        // creates the button to go back to the main menu
        generateBackButton();

        // instantiates background
        background = new Texture(Gdx.files.internal("cloudBGSmall.png"));
        // curent position of background
        currentBgY = game.GAME_WORLD_HEIGHT;
        // last time background looped
        lastTimeBg = TimeUtils.nanoTime();
    }

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

        // begin a new batch and draw the charShape and all coins, and scoreboard
        game.batch.begin();
        game.batch.draw(background, 0, currentBgY - game.GAME_WORLD_HEIGHT);
        game.batch.draw(background, 0, currentBgY);

        Scoreboard sb = Scoreboard.getInstance();
        sb.renderScoreboard(game, game.GAME_WORLD_HEIGHT);
        game.character.render(game.batch);
        game.cp.renderCoinPath(game.batch);
        game.powerPath.render(game.batch);
        game.batch.end();

        // draw the button
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        stage.setDebugAll(false);


        // move the background separator each 1s
        if(TimeUtils.nanoTime() - lastTimeBg > 10000000){
            // move the separator 1px
            currentBgY += 1;
            // set the current time to lastTimeBg
            lastTimeBg = TimeUtils.nanoTime();
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

}

