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
 * Created by William Schiela on 4/25/2015.
 */
// a class for the login screen where a user can enter their name before proceeding to main menu
public class LoginScreen implements Screen {
    private  FitViewport viewport;
    private GameState game;
    //private OrthographicCamera camera;
    private Texture background;
    private Stage stage;
    private Table table;
    private TextField inputField;
    private TextButton okButton;
    private Sprite sprite;

//    private int GAME_WORLD_WIDTH = 400;
//    private int GAME_WORLD_HEIGHT = 640;
//    private float aspectRatio = (float) Gdx.graphics.getWidth() / (float) Gdx.graphics.getHeight();

    public static String username;

    public LoginScreen(final GameState gam) {
        game = gam;
        background = new Texture(Gdx.files.internal("menuBG2.png"));
        sprite = new Sprite(background);
        sprite.setSize(game.GAME_WORLD_WIDTH,game.GAME_WORLD_HEIGHT);

//        float w = Gdx.graphics.getWidth();
//        float h = Gdx.graphics.getHeight();
//        camera = new OrthographicCamera(GAME_WORLD_HEIGHT * aspectRatio, GAME_WORLD_HEIGHT);
//        camera.position.set(GAME_WORLD_WIDTH/2f,GAME_WORLD_HEIGHT/2f,0);

        //camera.setToOrtho(false, w, h);
        //camera.translate(0,0,0);
        //camera.translate(0,215);
//        System.out.println("camera: " + camera.viewportWidth + ", " + camera.viewportHeight);
//        System.out.println("bg: " + background.getWidth() + ", " + background.getHeight());

//        viewport = new FitViewport(w, h, camera);
//        viewport.apply();
//        System.out.println("camera: " + camera.viewportWidth + ", " + camera.viewportHeight);
//        System.out.println("bg: " + background.getWidth() + ", " + background.getHeight());

        //camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0);

        //camera.update();
        //initializes menu screen items
        init();
    }

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

    public void render(float delta) {
        game.camera.update();
        Gdx.gl.glClearColor(.005f, .006f, .121f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.setProjectionMatrix(game.camera.combined);
        //game.batch.draw(sprite);
        //game.batch.draw(background, 0, 0);
        sprite.draw(game.batch);
        game.batch.end();

        //draw all items in table
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        stage.setDebugAll(true);

        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            System.out.println("touchposx: " + touchPos.x + "; " + touchPos.y);
        }


    }

    @Override
    public void show() {

    }
    @Override
    public void resize(int width, int height) {
//        viewport.update(width, height);
  //      stage.getViewport().update(width, height); //Stage viewport
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
