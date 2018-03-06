package org.creditsuisse.cli.command;

import org.creditsuisse.cli.CommandLineManager;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public class RectangleCommand extends AbstractCommand {

    private int x1;
    private int y1;
    private int x2;
    private int y2;

    private Pattern pattern = Pattern.compile("R\\s\\d{1,3}\\s\\d{1,3}\\s\\d{1,3}\\s\\d{1,3}");

    @Override
    public void init(CommandLineManager commandLineManager, List<String> parameters) {
        Iterator<String> paramsIter = parameters.iterator();
        this.x1 = typeParser.parseInteger(paramsIter.next());
        this.y1 = typeParser.parseInteger(paramsIter.next());
        this.x2 = typeParser.parseInteger(paramsIter.next());
        this.y2 = typeParser.parseInteger(paramsIter.next());
        this.commandLineManager = commandLineManager;
    }

    @Override
    public void execute() {
        commandLineManager.getCurrentCanvas().rectangle(x1, y1, x2, y2);
        commandLineManager.getCurrentCanvas().paint();
    }

    @Override
    public Pattern getPattern() {
        return pattern;
    }
}
