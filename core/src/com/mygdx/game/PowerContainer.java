package com.mygdx.game;

import com.badlogic.gdx.utils.TimeUtils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * This class holds the "powers" of the character.
 * It is dependent on the "character" class for functionality"
 */
public class PowerContainer {
    private Set<Power> powers;


    // constructor of power container
    public PowerContainer(int sW, int sH) {
        powers = new HashSet<Power>();
    }


    // remove the power after a given amount of time
    public void update() {
        Iterator<Power> iter = powers.iterator();
        while (iter.hasNext()) {
            Power p = iter.next();
            // disable power after specified amount
            if (TimeUtils.millis() - p.getTimeSpawned() > 15000) {
                //System.out.println("REMOVED POWER");
                iter.remove();
                Scoreboard sb = Scoreboard.getInstance();
                sb.setMultiplier(sb.getMultiplier() / p.getMultiplier());
            }
        }
    }

    // determine if is character is poisoned
    public boolean isPoisoned() {
        for (Power p : powers) {
            if (p instanceof PoisonBottle) return true;
        }
        return false;
    }


    // Add collected power to power container
    public void addPower(Power p) {
        powers.add(p);
        Scoreboard sb = Scoreboard.getInstance();
        sb.setMultiplier(sb.getMultiplier() * p.getMultiplier());
    }
}