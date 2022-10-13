package com.elo7.marsrover.web.controller.v1.request;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PlanetRequest(String planetName, int maxX, int maxY) { }
