package com.pacman;

import java.awt.Color;

/**
 * La representation de la classe Pacman
 */
public class Pacman extends Character {

    StatePower power;
    private boolean mouthOpen = true;

    public Pacman(int code, int speed) {
        super(code, speed);
        power = new StatePowerImpl();
        setColor(Color.WHITE);
    }

    public void setPower(StatePower power) {
        this.power = power;
    }

    public boolean isMouthOpen() {
        return mouthOpen;
    }

    public void setMouthOpen(boolean state) {
        this.mouthOpen = state;
    }

}
