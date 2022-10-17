package com.elo7.marsrover.command;

import com.elo7.marsrover.model.Rover;

public interface Command {
    public void execute(Rover rover) throws Exception;
}
