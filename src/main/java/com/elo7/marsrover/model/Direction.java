package com.elo7.marsrover.model;

public enum Direction {

    NORTH(0,1) {
        @Override
        public Direction left() {
            return WEST;
        }

        @Override
        public Direction right() {
            return EAST;
        }
    },

    SOUTH(0,-1) {
        @Override
        public Direction right() {
            return WEST;
        }

        @Override
        public Direction left() {
            return EAST;
        }
    },

    EAST(1,0) {
        @Override
        public Direction right() {
            return SOUTH;
        }

        @Override
        public Direction left() {
            return NORTH;
        }
    },

    WEST(-1,0) {
        @Override
        public Direction right() {
            return NORTH;
        }

        @Override
        public Direction left() {
            return SOUTH;
        }
    };

    private int xAxis;
    private int yAxis;

   Direction(int x, int y) {
        this.xAxis = x;
        this.yAxis = y;
    }

    public abstract Direction right();
    public abstract Direction left();
}
