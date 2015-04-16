package com.mygdx.game;

import com.badlogic.gdx.utils.TimeUtils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by acole9 on 4/15/15.
 */
public class PowerContainer {
    private Set<Power> powers;


    public PowerContainer(int sW, int sH) {
        powers = new HashSet<Power>();
    }

    public void update() {
        Iterator<Power> iter = powers.iterator();
        while (iter.hasNext()) {
            Power p = iter.next();
            p.update();
            if (TimeUtils.millis() - p.getTimeSpawned() > 15000) {
                //System.out.println("REMOVED POWER");
                iter.remove();
            }
        }
    }

    public boolean isPoisoned() {
        System.out.println(powers.size());
        for (Power p : powers) {
            //System.out.println("we have a power?");
            return true;
            //if (p instanceof PoisonBottle) return true;
        }
        return false;
    }

    public void addPower(Power p) {
        powers.add(p);
        //System.out.println("power collected " + powers.size());
    }
}
