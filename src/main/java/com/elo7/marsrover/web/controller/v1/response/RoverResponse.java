package com.elo7.marsrover.web.controller.v1.response;

import com.elo7.marsrover.model.Rover;

public record RoverResponse(Rover rover, String planetName) { }
