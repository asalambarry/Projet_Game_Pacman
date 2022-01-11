package com.pacman;

import java.awt.Color;

/**
 * La classe de personnage de base
 */
abstract class Character {

    private int code;
    private Color color;
    private StateMovable movable;

    public Character(int code, int speed) {
        this.code = code;
        movable = new StateMovableImpl(speed);
    }

    public void setStateMovable(StateMovable movable) {
        this.movable = movable;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getX() {
        return movable.getX();
    }

    public void setX(int x) {
        movable.setX(x);
    }

    public int getY() {
        return movable.getY();
    }

    public void setY(int y) {
        movable.setY(y);
    }

    public int getSpeed() {
        return movable.getSpeed();
    }

    public void setSpeed(int speed) {
        movable.setSpeed(speed);
    }

    public Direction getDirection() {
        return movable.getDirection();
    }

    public void setDirection(Direction direction) {
        movable.setDirection(direction);
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "{" + movable.getX() + "," + movable.getY() + ", " + movable.getDirection() + "}";
    }
}
