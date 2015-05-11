package com.mygdx.game;

/**
 * Create powers in game (will become more relevant with more power ups)
 */
public class PowerFactory {
    public static Power createPower() {
        double rand = Math.random();
        if (rand <= GameState.difficultySetting/6.0) {
            return new PoisonBottle();
        }
        else if (rand <= 0.8) {
            return new Times2Multiplier();
        }
        else {
            return new Rainbow();
        }
    }
}
