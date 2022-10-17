package com.elo7.marsrover.command;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class StringCommandParser {

    private static final Map<CommandInstructions, Command> stringToCommand = new EnumMap<>(CommandInstructions.class) {{
        put(CommandInstructions.L, new TurnLeftCommand());
        put(CommandInstructions.R, new TurnRightCommand());
        put(CommandInstructions.M, new MoveCommand());
    }};

    public static List<Command> parseToCommands(List<CommandInstructions> commandInstructions) {
        var commandsParsed = new ArrayList<Command>();
        log.info("Parsing: {} array: ", commandInstructions);
        for (CommandInstructions command : commandInstructions) {
            if (command == null) break;
            var c = stringToCommand.get(command);
            commandsParsed.add(c);
            log.info("command: {} c: {} commmandParsed: {}", command, c, commandsParsed);
        }
        return commandsParsed;
    }

    public enum CommandInstructions {
        L,
        R,
        M
    }
}
