package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Responsible for the obstacle ups generation, display, and collection.
 */
public class ObstaclePath {
    public Set<Obstacle> obstaclePath;
    private long lastObstacleSpawn;
    int screenWidth;
    int screenHeight;

    public ObstaclePath(int sW, int sH) {
        obstaclePath = new HashSet<Obstacle>();
        lastObstacleSpawn = TimeUtils.millis();
        screenWidth = sW;
        screenHeight = sH;
    }

    public void update(Character character) {
        // determine if enough time has passed since last obstacle spawn
        if (TimeUtils.millis() - lastObstacleSpawn > 20000 && Math.random() > .9) {
            spawnObstacle();
        }

        // update state of obstacles
        Iterator<Obstacle> iter = obstaclePath.iterator();
        while (iter.hasNext()) {
            Obstacle obstacle = iter.next();
            // if obstacle is above screen, remove
            if (obstacle.getY() > screenHeight) {
                iter.remove();
                obstacle.dispose();
            }
            // if obstacle overlaps with character, hit character
            else if (obstacle.getRectangle().overlaps(character.charShape)
                    && !character.isTransperent()) {
                if (obstacle.collisionSound != null) obstacle.collisionSound.play();
                iter.remove();
                character.collision();
            }
            // else update obstacle
            else obstacle.update();
        }
    }

    private void spawnObstacle() {
        Obstacle obstacle = ObstacleFactory.createObstacle();
        if (obstacle.spawnSound != null) obstacle.spawnSound.play();
        obstaclePath.add(obstacle);
        lastObstacleSpawn = TimeUtils.millis();
    }


    public void render(SpriteBatch batch) {
        for (Obstacle obstacle : obstaclePath) {
            obstacle.render(batch);
        }
    }

}