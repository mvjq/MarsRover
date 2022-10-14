package com.elo7.marsrover.web.controller.v1.request;

import com.elo7.marsrover.model.Command;

import java.util.List;
public record CommandRequest(List<Command> commands) {}
