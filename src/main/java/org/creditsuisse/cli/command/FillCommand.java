package org.creditsuisse.cli.command;

import org.creditsuisse.cli.CommandLineManager;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public class FillCommand extends AbstractCommand {
    private int x;
    private int y;
    private char color;
    private Pattern pattern = Pattern.compile("B\\s\\d{1,3}\\s\\d{1,3}\\s[!-~]{1}");

    @Override
    public void init(CommandLineManager commandLineManager, List<String> parameters) {
        super.init(commandLineManager, parameters);
        Iterator<String> paramsIter = parameters.iterator();
        this.x = typeParser.parseInteger(paramsIter.next());
        this.y = typeParser.parseInteger(paramsIter.next());
        this.color = typeParser.parseCharacter(paramsIter.next());
    }

    @Override
    public void execute() {
        commandLineManager.getCurrentCanvas().fill(x,y,color);
        commandLineManager.getCurrentCanvas().paint();
    }

    @Override
    public Pattern getPattern() {
        return pattern;
    }
}
