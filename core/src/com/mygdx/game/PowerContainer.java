package com.mygdx.game;

import com.badlogic.gdx.utils.TimeUtils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *C This class holds the "powers" of the character.
 * It is dependent on the "character" class for functionality"
 */
public class PowerContainer {
    private Set<Power> powers;


    // constructor of power container
    public PowerContainer(int sW, int sH) {
        powers = new HashSet<Power>();
    }


    // update position of "powers"
    public void update() {
        Iterator<Power> iter = powers.iterator();
        while (iter.hasNext()) {
            Power p = iter.next();
            p.update();
            // disable power after specified amount
            if (TimeUtils.millis() - p.getTimeSpawned() > 15000) {
                //System.out.println("REMOVED POWER");
                iter.remove();
                // TODO: DIVIDE BY SCOREBOARD MULTIPLIER BY POWER MULTIPLIER
            }
        }
    }

    // determine if is character is poisoned
    public boolean isPoisoned() {
        //System.out.println(powers.size());
        for (Power p : powers) {
            //System.out.println("we have a power?");
            return true;
            //if (p instanceof PoisonBottle) return true;
        }
        return false;
    }


    // Add collected power to power container
    public void addPower(Power p) {
        powers.add(p);
        // TODO: MULTIPLY CURRENT SCOREBOARD MULTIPLIER BY POWER MULTIPLIER
    }
}
