package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
import com.badlogic.gdx.utils.TimeUtils;


public class GameEndScreen implements Screen {
    private GameState game;
    private Texture background;
    private Stage stage;
    private Sprite sprite;
    long startTime;
    // character instance that falls gradually while screen is shown
    Character character = new Character(game.GAME_WORLD_WIDTH, game.GAME_WORLD_HEIGHT);
    // determines if character orientation is right or left
    boolean isCharacterShiftingLeft;
    // applause sound when game over screen is shown
    private Sound gameOverSound;
    // game over music
    public Music gameOverMusic; // end game music



    /**
     * Game End screen constructor. Instantiates the background and initializes the scoreboard,
     * back button and fields.
     * @param gam
     */
    public GameEndScreen(final GameState gam){
        game = gam;
        background = new Texture(Gdx.files.internal("gameOverBG.png"));
        sprite = new Sprite(background);
        sprite.setSize(game.GAME_WORLD_WIDTH,game.GAME_WORLD_HEIGHT);

        // creates the button to go back to the main menu
        generateQuitButton();

        startTime = TimeUtils.nanoTime();

        // set character starting pos
        character.setVerticalPos((int)(game.GAME_WORLD_HEIGHT));
        character.setHorizontalPos((int)(game.GAME_WORLD_WIDTH/2 - character.charShape.width/2));

        // set audio files for game over sounds and music
        gameOverSound = Gdx.audio.newSound(Gdx.files.internal("applause.wav"));
        gameOverSound.play();
        gameOverMusic = Gdx.audio.newMusic(Gdx.files.internal("gameOverSong.mp3"));
        gameOverMusic.play();
    }
    /**
     * Renders the login screen
     * @param delta
     */
    @Override
    public void render(float delta) {
        game.camera.update();
        //character.update();
        Gdx.gl.glClearColor(.005f, .006f, .121f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // render the screen background
        game.batch.begin();
        game.batch.setProjectionMatrix(game.camera.combined);
        sprite.draw(game.batch);
        character.render(game.batch);
        System.out.println("cx: " + character.getX() + "; cy: " + character.charShape.y);
        Scoreboard sb = Scoreboard.getInstance();
        sb.renderGameOverScoreboard(game, game.GAME_WORLD_HEIGHT);
        game.batch.end();

        System.out.println(character.charShape.x);
        // draw the back button
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        stage.setDebugAll(false);
        // make character fall from right to left
        updateFallingCharaterPosition();
    }

    /**
     * Character position falls and moves right to left gradually.
     */
    public void updateFallingCharaterPosition() {
        if (TimeUtils.timeSinceNanos(startTime) > 10000000) {
            character.shiftCharacterVertically(.5f);
            if (isCharacterShiftingLeft) {
                character.shiftCharacterHorizontally(-.3f);
                character.orientation = characterOrientation.LEFT;
                if (character.getX() < game.GAME_WORLD_WIDTH * 0.2) isCharacterShiftingLeft = !isCharacterShiftingLeft;
            } else {
                character.shiftCharacterHorizontally(0.3f);
                character.orientation = characterOrientation.RIGHT;
                if (character.getX() > game.GAME_WORLD_WIDTH * .8) isCharacterShiftingLeft = !isCharacterShiftingLeft;
            }

            startTime = TimeUtils.nanoTime();
        }
    }

    /**
     * Genrates the quit button that quits game
     */
    private void generateQuitButton () {
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
        TextButton quitButton = new TextButton("QUIT", buttonStyle);
        buttonTable.add(quitButton);
        buttonTable.bottom().padBottom(125);
        stage.addActor(buttonTable);

        //add a listener for the back button
        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                //quit the app
                Gdx.app.exit();
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
        gameOverMusic.stop();
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        gameOverMusic.stop();
    }

    @Override
    public void dispose() {
    }
}
