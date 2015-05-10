package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Generates the main menu screen with options to quit, change settings, and play game
 */
public class MainMenu implements Screen {

    final GameState game;
    private Stage stage;
    private Texture background;


    /**
     * Constructor for main menu screen that initializes menu buttons and background
     * @param gam
     */
    public MainMenu(final GameState gam) {
        game = gam;
        //initializes menu screen items
        initialize();
        background = new Texture(Gdx.files.internal("menuBG2.png"));
    }

    /**
     * Initializes main menu buttons table including PLAY, SETTINGS, and QUIT buttons.
     */
    public void initialize(){
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

        //create play button
        TextButton playButton = new TextButton("PLAY", buttonStyle);
        buttonTable.add(playButton);
        buttonTable.row();

        //create settings button
        TextButton settingsButton = new TextButton("SETTINGS", buttonStyle);
        buttonTable.add(settingsButton);
        buttonTable.row();

        //create quit button
        TextButton quitButton = new TextButton("QUIT", buttonStyle);
        buttonTable.add(quitButton);

        stage.addActor(buttonTable);

        //add a listener for play button
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                System.out.println("CLICKED");
                //move to gameplay screen
               // game.instantiateDataFile();
                game.setScreen(new GameScreen(game));
            }
        });
        playButton.setPosition(600,400);

        //add a listener for settings button
        settingsButton.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                //move to settings screen
                game.setScreen(new SettingScreen(game));
            }
        });


        //add a listener for the quit button
        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                //System.out.println("CLICKED");
                //quit the app
                Gdx.app.exit();
            }
        });

    }


    /**
     * Renders menu screen with background and main menu buttons
     * @param delta
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.005f, .006f, .121f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.camera.update();

        // draw background
        game.batch.begin();
        game.batch.draw(background, 0, 0);
        game.batch.end();


        //draw all items in buttonTable
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        stage.setDebugAll(false);
     }

    @Override
    public void show() {
    }


    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {

    }
}
