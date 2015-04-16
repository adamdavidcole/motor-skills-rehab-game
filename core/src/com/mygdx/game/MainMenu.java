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


public class MainMenu implements Screen {

    final MyGdxGame game;
    private Texture buttonTexture;
    private TextButton playButton;
    private TextButton quitButton;
    private Table buttonTable;
    private Stage stage;
    private Texture background;

    OrthographicCamera camera;

    public MainMenu(final MyGdxGame gam) {
        game = gam;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 1280);
        //initializes menu screen items
        initialize();
        background = new Texture(Gdx.files.internal("menuBG.png"));


    }

    public void initialize(){
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        //create the stage for buttons
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);


        //create button table to hold textbuttons: play and quit
        buttonTable = new Table();
        buttonTable.setFillParent(true);


        //create button style
        buttonTexture = new Texture(Gdx.files.internal("MainMenuButton.png"));
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = game.font;
        buttonStyle.up = new TextureRegionDrawable(new TextureRegion(buttonTexture));

        //create play button
        playButton = new TextButton("PLAY", buttonStyle);
        buttonTable.add(playButton);
        buttonTable.row();
        //create quit button
        quitButton = new TextButton("QUIT", buttonStyle);
        buttonTable.add(quitButton);
        stage.addActor(buttonTable);

        //add a listener for play button
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                //System.out.println("CLICKED");
                //move to gameplay screen
                game.setScreen(new GameScreen(game));
            }
        });
        playButton.setPosition(600,400);

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

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        game.batch.begin();
        game.batch.draw(background, 0, 0);

        /*game.font.draw(game.batch,"Welcome to Irish Frenzy!!! ",100,150);
        game.font.draw(game.batch,"Tap anywhere to begin!",100,100);*/
        game.batch.end();
        //draw all items in buttonTable

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        stage.setDebugAll(true);

            /*if(Gdx.input.isTouched())

            {
                game.setScreen(new GameScreen(game));
                dispose();
            }*/
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
