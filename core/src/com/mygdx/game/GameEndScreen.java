package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


public class GameEndScreen implements Screen {
    private GameState game;
    private Texture background;
    private Stage stage;
    private Sprite sprite;

    /**
     * Game End screen constructor. Instantiates the background and initializes the scoreboard, back button
     * and fields.
     * @param gam
     */
    public GameEndScreen(final GameState gam){
        game = gam;
        background = new Texture(Gdx.files.internal("menuBG2.png"));
        sprite = new Sprite(background);
        sprite.setSize(game.GAME_WORLD_WIDTH,game.GAME_WORLD_HEIGHT);
        // creates the button to go back to the main menu
        generateBackButton();

    }
    /**
     * Renders the login screen
     * @param delta
     */
    @Override
    public void render(float delta) {
        game.camera.update();
        Gdx.gl.glClearColor(.005f, .006f, .121f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // render the screen background
        game.batch.begin();
        game.batch.setProjectionMatrix(game.camera.combined);
        sprite.draw(game.batch);
        Scoreboard sb = Scoreboard.getInstance();
        sb.renderScoreboard(game, game.GAME_WORLD_HEIGHT);
        game.batch.end();


        // draw the back button
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        stage.setDebugAll(false);
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

        //create back button
        TextButton backButton = new TextButton("BACK", buttonStyle);
        buttonTable.add(backButton);
        buttonTable.bottom().padBottom(125);
        stage.addActor(buttonTable);

        //add a listener for the back button
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                //back to main menu of the app
                game.setScreen(new MainMenu(game));
            }
        });
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
