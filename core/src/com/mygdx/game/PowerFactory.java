package com.mygdx.game;

/**
 * Created by acole9 on 4/15/15.
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
