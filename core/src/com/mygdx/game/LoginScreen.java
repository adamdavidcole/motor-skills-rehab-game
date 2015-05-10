package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Generates the login screen so the user can input his username to be saved with the datafile
 */
// a class for the login screen where a user can enter their name before proceeding to main menu
public class LoginScreen implements Screen {
    private  FitViewport viewport;
    private GameState game;
    private Texture background;
    private Stage stage;
    private Table table;
    private TextField inputField;
    private TextButton okButton;
    private Sprite sprite;

    public static String username;

    /**
     * Login screen constructor. Instantiates the background and initializes the login
     * buttons and fields.
     * @param gam
     */
    public LoginScreen(final GameState gam) {
        game = gam;
        background = new Texture(Gdx.files.internal("menuBG2.png"));
        sprite = new Sprite(background);
        sprite.setSize(game.GAME_WORLD_WIDTH,game.GAME_WORLD_HEIGHT);

        init();
    }

    /**
     * Initializes the login screen and buttons
     */
    private void init() {
        game.camera.update();
        game.batch.setProjectionMatrix(game.camera.combined);

        //create the stage for buttons
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);


        // create a table to hold username textfield and "ok" button
        table = new Table();
        table.setFillParent(true);


        //create the text field for getting the user's name
        TextField.TextFieldStyle fieldStyle = new TextField.TextFieldStyle();
        fieldStyle.font = game.font;
        fieldStyle.fontColor = new Color(Color.BLACK);
        inputField = new TextField("Enter your name here.", fieldStyle);
        table.add(inputField);

        //create the "ok" button
        Texture buttonTexture = new Texture(Gdx.files.internal("MainMenuButton.png"));
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = game.font;
        buttonStyle.up = new TextureRegionDrawable(new TextureRegion(buttonTexture));
        okButton = new TextButton("OK", buttonStyle);
        okButton.setPosition(600,400);
        table.row();
        table.add(okButton);
        stage.addActor(table);

        // add a listener for the text field
        inputField.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                inputField.setText("");
            }
        });

        //add a listener for "ok" button
        okButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // save username and move to menu screen
                username = inputField.getText();
                game.instantiateDataFile();
                game.setScreen(new MainMenu(game));
            }
        });

    }

    /**
     * Renders the login screen
     * @param delta
     */
    public void render(float delta) {
        game.camera.update();
        Gdx.gl.glClearColor(.005f, .006f, .121f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // render the screen background
        game.batch.begin();
        game.batch.setProjectionMatrix(game.camera.combined);
        sprite.draw(game.batch);
        game.batch.end();

        //draw all items in table
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        stage.setDebugAll(true);
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
