package com.elo7.marsrover.web.controller.v1.request;

import com.elo7.marsrover.model.Direction;
import com.elo7.marsrover.model.Point;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record RoverRequest(String roverName, String planetName, Direction landingDirection, Point landingPoint) {}
