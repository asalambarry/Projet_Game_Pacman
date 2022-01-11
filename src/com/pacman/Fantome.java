package com.pacman;

import java.awt.Color;

/**
 * La représentation de l'entité Fantôme
 */
public class Fantome extends Character {

    private int smallStepX = 1;
    private int smallStepY = 1;

    public Fantome(int code, int speed, Direction initialDirection) {
        super(code, speed);
        setColor(new Color(250, 60, 0));
        setDirection(initialDirection);
    }

    public int getSmallStepX() {
        return this.smallStepX;
    }

    public void setSmallStepX(int smallStepX) {
        this.smallStepX = smallStepX;
    }

    public int getSmallStepY() {
        return this.smallStepY;
    }

    public void setSmallStepY(int smallStepY) {
        this.smallStepY = smallStepY;
    }
    
}
