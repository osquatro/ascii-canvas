package org.creditsuisse.cli.command;

import org.creditsuisse.cli.CommandLineManager;
import org.creditsuisse.cli.ValidationException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.creditsuisse.common.Constants.CANVAS_NOT_CREATED_ERROR;
import static org.creditsuisse.common.Constants.COMMAND_DELIMITER;
import static org.creditsuisse.common.Constants.UNKNOWN_OR_MALFORMED_COMMAND_ERROR;

public class CommandFactory {

    private static Map<CommandCode, Supplier<AbstractCommand>> COMMAND_CREATORS = new HashMap<>();

    static {
        COMMAND_CREATORS.put(CommandCode.CANVAS, () -> new CanvasCommand());
        COMMAND_CREATORS.put(CommandCode.LINE, () -> new LineCommand());
        COMMAND_CREATORS.put(CommandCode.RECTANGLE, () -> new RectangleCommand());
        COMMAND_CREATORS.put(CommandCode.FILL, () -> new FillCommand());
        COMMAND_CREATORS.put(CommandCode.QUIT, () -> new QuitCommand());
        COMMAND_CREATORS.put(CommandCode.MALFORMED, () -> new MalformedCommand());
    }

    private enum CommandCode {
        CANVAS("C"),
        LINE("L"),
        RECTANGLE("R"),
        FILL("B"),
        QUIT("Q"),
        MALFORMED("");

        private String code;

        CommandCode(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public static CommandCode fromString(String str) {
            for (CommandCode commandCode : values()) {
                if (commandCode.getCode().equals(str)) {
                    return commandCode;
                }
            }
            return MALFORMED;
        }
    };

    public AbstractCommand createCommand(CommandLineManager commandLineManager, String line) {
        AbstractCommand commandEntity = null;
        try {
            CommandCode commandCode = getCommand(line);
            boolean isCanvasReady = commandLineManager.getCurrentCanvas() != null;

            if (validateCondition(isCanvasReady, commandCode)) {
                commandEntity = COMMAND_CREATORS.get(commandCode).get();
                if (commandCode == CommandCode.MALFORMED) {
                    commandEntity.init(commandLineManager, Arrays.asList(UNKNOWN_OR_MALFORMED_COMMAND_ERROR));
                } else {
                    commandEntity.validateCommandFormat(line);
                    List<String> parameters = Stream.of(line.split(COMMAND_DELIMITER)).skip(1).collect(Collectors.toList());
                    commandEntity.init(commandLineManager, parameters);
                }
            } else {
                commandEntity = prepareMalformedCommand(commandLineManager, CANVAS_NOT_CREATED_ERROR);
            }
        } catch (ValidationException e) {
            commandEntity = prepareMalformedCommand(commandLineManager, e.getMessage());
        }
        return commandEntity;
    }

    private CommandCode getCommand(String commandString) {
        return (null == commandString || commandString.length() == 0) ?
            CommandCode.MALFORMED :
                CommandCode.fromString(String.valueOf(commandString.charAt(0)));
    }

    private boolean validateCondition(boolean isCanvasReady, CommandCode commandCode) {
        if (!isCanvasReady && commandCode != CommandCode.CANVAS && commandCode != CommandCode.QUIT) {
            return false;
        }
        return true;
    }

    private AbstractCommand prepareMalformedCommand(CommandLineManager commandLineManager, String errorCode) {
        AbstractCommand commandEntity = new MalformedCommand();
        commandEntity.init(commandLineManager, Arrays.asList(errorCode));
        return commandEntity;
    }
}
