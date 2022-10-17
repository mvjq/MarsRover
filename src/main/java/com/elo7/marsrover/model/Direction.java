package com.elo7.marsrover.model;

public enum Direction {
    N,
    W,
    E,
    S;

    private Direction left;
    private Direction right;

    private Point movePoint;

    static {
        N.left = W;
        N.right = E;
        N.movePoint = new Point(0, +1);

        S.left = W;
        S.right = E;
        S.movePoint = new Point(0, -1);


        E.left = N;
        E.right = S;
        E.movePoint = new Point(+1, 0);

        W.left = S;
        W.right = N;
        W.movePoint = new Point(-1, 0);
    }

    public Direction getLeft() {
        return this.left;
    }

    public Direction getRight() {
        return this.right;
    }

    public Point getMovePoint() {
        return this.movePoint;
    }
}
