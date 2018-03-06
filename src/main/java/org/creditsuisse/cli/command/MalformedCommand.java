package org.creditsuisse.cli.command;

import org.creditsuisse.cli.CommandLineManager;

import java.util.List;
import java.util.regex.Pattern;

public class MalformedCommand extends AbstractCommand {

    private String messageCode;

    @Override
    public void init(CommandLineManager commandLineManager, List<String> parameters) {
        super.init(commandLineManager, parameters);
        this.messageCode = parameters.iterator().next();
    }

    @Override
    public void execute() {
        this.commandLineManager.printMessage(messageCode);

    }

    @Override
    public Pattern getPattern() {
        throw new UnsupportedOperationException();
    }
}
