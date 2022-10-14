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

    public Point movePoint(Point newPoint) {
        this.x += newPoint.x;
        this.y += newPoint.y;
        return this;
    }
}
