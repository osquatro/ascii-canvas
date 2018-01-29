package org.creditsuisse.cli;

public enum Command {

    CANVAS("C"),
    LINE("L"),
    RECTANGLE("R"),
    FILL("B"),
    QUIT("Q");


    private String commandName;

    private Command(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }

    public static Command fromString(String str) {
        for (Command command : values()) {
            if (command.getCommandName().equals(str)){
                return command;
            }
        }

        return null;
    }
}
