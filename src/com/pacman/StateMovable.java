package com.pacman;

/**
 * Le pattern state pour deplacer les entités
 */
public interface StateMovable {
    int getSpeed();
    void setSpeed(int speed);
    Direction getDirection();
    void setDirection(Direction direction);
    int getX();
    void setX(int x);
    int getY();
    void setY(int y);
}
