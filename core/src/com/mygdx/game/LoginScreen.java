package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

/**
 * Created by William Schiela on 4/25/2015.
 */
// a class for the login screen where a user can enter their name before proceeding to main menu
public class LoginScreen implements Screen {
    private GameState game;
    private OrthographicCamera camera;
    private Texture background;
    private Stage stage;
    private Table table;
    private TextField inputField;
    private TextButton okButton;

    public static String username;

    public LoginScreen(final GameState gam) {
        game = gam;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 1280);
        //initializes menu screen items
        init();
        background = new Texture(Gdx.files.internal("menuBG.png"));
    }

    private void init() {
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

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
                game.setScreen(new MainMenu(game));
            }
        });

    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(background, 0, 0);

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
