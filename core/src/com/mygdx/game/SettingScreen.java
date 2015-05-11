package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


/**
 * Created by Anthony on 5/4/2015.
 */
public class SettingScreen implements Screen {
    final GameState game;
    private Label diffSliderLabel;
    private Label timeSliderLabel;
    private Label rangeSliderLabel;
    private Stage stage;
    private Texture background;
    private Skin skin;


    public SettingScreen(final GameState gam) {
        this.game = gam;

        initialize();
        background = new Texture(Gdx.files.internal("menuBG2.png"));

    }

    public void initialize() {
        game.camera.update();
        game.batch.setProjectionMatrix(game.camera.combined);

        //create the stage for sliders
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        //create the table for the sliders
        Table sliderTable = new Table();
        sliderTable.setFillParent(true);
        sliderTable.bottom().padBottom(50);

        //create diffSlider
        diffSliderLabel = new Label("Difficulty: Very Easy", skin);
        Slider diffSlider = new Slider(1f, 5f, 1f, false, skin);
        sliderTable.add(diffSliderLabel);
        sliderTable.row();
        sliderTable.add(diffSlider);
        sliderTable.row();

        //create rangeSlider
        rangeSliderLabel = new Label("Range of Motion: Low", skin);
        Slider rangeSlider = new Slider(1f, 3f, 1f, false, skin);
        sliderTable.add(rangeSliderLabel);
        sliderTable.row();
        sliderTable.add(rangeSlider);
        sliderTable.row();

        //create timeSlider
        timeSliderLabel = new Label("Game Play Duration: 1 Minute", skin);
        Slider timeSlider = new Slider(1f, 15f, 1f, false, skin);
        sliderTable.add(timeSliderLabel);
        sliderTable.row();
        sliderTable.add(timeSlider);
        sliderTable.row();

        //create button style
        Texture buttonTexture = new Texture(Gdx.files.internal("MainMenuButton.png"));
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = game.font;
        buttonStyle.up = new TextureRegionDrawable(new TextureRegion(buttonTexture));

        //create play button
        TextButton playButton = new TextButton("PLAY", buttonStyle);
        sliderTable.add(playButton);


        stage.addActor(sliderTable);

        //add listener to difficulty slider
        diffSlider.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                int value = (int)((Slider) actor).getValue();
                GameState.difficultySetting = value;
                game.gameScrollSpeed = 100 * value;
                updateDiffSliderLabel(value);
            }
        });

        //add listener to time slider
        timeSlider.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                int value = (int)((Slider) actor).getValue();
                GameState.gameDurationSetting = value;
                updateTimeSliderLabel(value);
            }
        });

        //add listener to range of motion slider
        rangeSlider.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                int value = (int)((Slider) actor).getValue();
                GameState.rangeOfMotionSetting = value;
                updateRangeSliderLabel(value);
            }
        });

        //add a listener for play button
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                System.out.println("CLICKED");
                //move to gameplay screen
                game.setScreen(new GameScreen(game));
            }
        });
    }

    //updates difficulty label to slider value
    private void updateDiffSliderLabel(int value){
        if (value == 1){
          diffSliderLabel.setText("Difficulty: Very Easy");
        }
        else if (value == 2){
            diffSliderLabel.setText("Difficulty: Easy");
        }
        else if (value == 3){
            diffSliderLabel.setText("Difficulty: Normal");
        }
        else if (value == 4){
            diffSliderLabel.setText("Difficulty: Hard");
        }
        else {
            diffSliderLabel.setText("Difficulty: Very Hard");
        }

    }

    //updates range of motion to slider value
    private void updateRangeSliderLabel(int value){
        if(value == 1){
            rangeSliderLabel.setText("Range of Motion: Low");
        }
        else if(value == 2) {
            rangeSliderLabel.setText("Range of Motion: Medium");
        }
        else{
            rangeSliderLabel.setText("Range of Motion: High");
        }
    }

    //updates time label to slider value
    private void updateTimeSliderLabel(int value){
        if(value == 1){
            timeSliderLabel.setText("Game Play Duration: " + value + " Minute");
        }
        else {
            timeSliderLabel.setText("Game Play Duration: " + value + " Minutes");
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.005f, .006f, .121f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


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
