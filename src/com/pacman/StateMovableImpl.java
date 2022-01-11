package com.pacman;

public class StateMovableImpl implements StateMovable {

    private int x, y;
    private int speed;
    private Direction direction = Direction.UP;

    public StateMovableImpl(int speed) {
        this.speed = speed;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSpeed() {
        return this.speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

}
