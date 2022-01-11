package com.pacman;

public class StatePowerImpl implements StatePower {
    
    private boolean isInvisible = false;
    private boolean isSuperPacman = false;

    public boolean isInvisible() {
        return this.isInvisible;
    }

    public void setInvisible(boolean isInvisible) {
        this.isInvisible = isInvisible;
    }

    public boolean isSuperPacman() {
        return this.isSuperPacman;
    }

    public void setSuperPacman(boolean isSuperPacman) {
        this.isSuperPacman = isSuperPacman;
    }
}
