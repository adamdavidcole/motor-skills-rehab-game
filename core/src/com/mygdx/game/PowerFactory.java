package com.mygdx.game;

/**
 * Create powers in game (will become more relevant with more power ups)
 */
public class PowerFactory {
    public static Power createPower() {
        double rand = Math.random();
        if (rand <= 0.5) {
            return new PoisonBottle();
        }
        else {
            return new Times2Multiplier();
        }
    }
}
