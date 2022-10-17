package com.elo7.marsrover.command;

import com.elo7.marsrover.model.Rover;

public class MoveCommand implements Command {

    @Override
    public void execute(Rover rover) throws Exception {
        rover.move();
    }
}
