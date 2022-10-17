package com.elo7.marsrover.model;


import lombok.Getter;

import javax.persistence.Embeddable;

@Getter
@Embeddable
public class Point {

    private int x;
    private int y;
    public Point() {}
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public Point moveNewPoint(Point newPoint) {
        return new Point(
                this.x + newPoint.x,
                this.y + newPoint.y
        );
    }
}
