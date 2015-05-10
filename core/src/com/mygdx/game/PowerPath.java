package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Responsible for the power ups generation, display, and collection.
 */
public class PowerPath {
    public Set<Power> powerPath;
    private long lastPowerSpawn;
    int screenWidth;
    int screenHeight;

    public int spawnInterval = 15000;

    public PowerPath(int sW, int sH) {
        powerPath = new HashSet<Power>();
        lastPowerSpawn = TimeUtils.millis();
        screenWidth = sW;
        screenHeight = sH;
    }

    public void update(Character character) {
        // determine if enough time has passed since last power spawn
        int currSpawnInterval = spawnInterval - GameState.difficultySetting * 2000;
        //System.out.println(GameState.difficulty);
        if (TimeUtils.millis() - lastPowerSpawn > currSpawnInterval && Math.random() > .5) {
            spawnPower();
        }

        // update state of powers
        Iterator<Power> iter = powerPath.iterator();
        while (iter.hasNext()) {
            Power p = iter.next();
            // if power is above screen, remove
            if (p.getY() > screenHeight) {
                iter.remove();
                p.dispose();
            }
            // if power overlaps with character, add power to character
            else if (p.getRectangle().overlaps(character.charShape) && !character.isTransperent()) {
                iter.remove();
                character.powers.addPower(p);
            }
            // else update power of power
            else p.update();
        }
    }

    private void spawnPower() {
        powerPath.add(PowerFactory.createPower());
        lastPowerSpawn = TimeUtils.millis();
    }


    public void render(SpriteBatch batch) {
        for (Power p : powerPath) {
            p.render(batch);
        }
    }

}