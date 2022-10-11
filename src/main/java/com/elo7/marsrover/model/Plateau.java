package com.elo7.marsrover.model;

public class Plateau {

    public Plateau(Point maxPoint) {
        this.maxPoint = maxPoint;
    }

    public Point maxPoint;

    public int getMaxX() {
        return maxPoint.getX();
    }

    public int getMaxY() {
        return maxPoint.getY();
    }
}
