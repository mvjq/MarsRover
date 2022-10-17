package com.elo7.marsrover.web.controller.v1.request;

import com.elo7.marsrover.command.StringCommandParser;

import java.util.List;
public record CommandRequest(List<StringCommandParser.CommandInstructions> commandInstructions) {}
