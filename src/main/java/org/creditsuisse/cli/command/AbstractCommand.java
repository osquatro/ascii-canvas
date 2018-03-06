package org.creditsuisse.cli.command;

import org.creditsuisse.cli.CommandLineManager;
import org.creditsuisse.cli.ValidationException;
import org.creditsuisse.common.TypeParser;

import java.io.Serializable;
import java.util.List;
import java.util.regex.Pattern;

import static org.creditsuisse.common.Constants.COMMAND_ARGUMENTS_MALFORMED;

public abstract class AbstractCommand implements Serializable {

    protected CommandLineManager commandLineManager;

    protected static final TypeParser typeParser = new TypeParser();

    public void init(CommandLineManager commandLineManager, List<String> parameters) {
        this.commandLineManager = commandLineManager;
    }

    public abstract void execute();

    public abstract Pattern getPattern();

    public void validateCommandFormat(String line) {
        if (!getPattern().matcher(line).matches()) {
            throw new ValidationException(COMMAND_ARGUMENTS_MALFORMED);
        }
    }
}
