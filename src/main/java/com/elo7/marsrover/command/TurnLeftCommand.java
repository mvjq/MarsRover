package com.elo7.marsrover.command;

import com.elo7.marsrover.model.Rover;

public class TurnLeftCommand implements Command {
    @Override
    public void execute(Rover rover) {
        rover.turnLeft();
    }
}
