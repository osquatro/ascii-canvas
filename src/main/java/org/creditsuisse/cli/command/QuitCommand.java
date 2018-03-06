package org.creditsuisse.cli.command;

import org.creditsuisse.cli.CommandLineManager;

import java.util.List;
import java.util.regex.Pattern;

public class QuitCommand extends AbstractCommand {

    private Pattern pattern = Pattern.compile("Q");

    @Override
    public void init(CommandLineManager commandLineManager, List<String> parameters) {
        this.commandLineManager = commandLineManager;
    }

    @Override
    public void execute() {
        commandLineManager.setDone(true);
    }

    @Override
    public Pattern getPattern() {
        return pattern;
    }
}
