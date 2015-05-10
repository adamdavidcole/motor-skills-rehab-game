package com.mygdx.game;

/**
 * Create powers in game (will become more relevant with more power ups)
 */
public class ObstacleFactory {
    public static Obstacle createObstacle() {
        return new Cannonball();

        /* WILL BECOME RELEVANT WITH MORE OBSTACLES
        double rand = Math.random();
        if (rand <= 0.5) {
            return new Cannonball();
        }
        else {
            return new ;
        }
        */
    }
}

