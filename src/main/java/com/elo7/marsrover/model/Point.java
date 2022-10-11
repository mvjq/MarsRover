package com.elo7.marsrover.model;

public class Point {

    private int x;
    private int y;

    public Point(int x, int y) throws Exception {
        if (y < 0 && x < 0) {
            throw new Exception("Point cannot be negative");
        }
        this.x = x;
        this.y = y;
    }

    public Point movePoint(Point newPoint) {
        this.x += newPoint.x;
        this.y += newPoint.y;

        return this;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
