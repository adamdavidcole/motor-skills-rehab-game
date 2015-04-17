package com.mygdx.game;

/**
 * Create powers in game (will become more relevant with more power ups)
 */
public class PowerFactory {
    public static Power createPower() {
        // TODO: when new powers are added, have factory create random power
        /*  double rand = Math.random();
          if (rand >= 0) {
            return new PoisonBottle();
        }*/
        return new PoisonBottle();
    }
}
