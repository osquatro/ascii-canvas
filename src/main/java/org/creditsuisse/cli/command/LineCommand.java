package org.creditsuisse.cli.command;

import org.creditsuisse.cli.CommandLineManager;
import org.creditsuisse.cli.ValidationException;
import org.creditsuisse.common.ValidationUtil;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import static org.creditsuisse.common.Constants.ONLY_HORIZONTAL_OR_VERTICAL_LINES_ARE_SUPPORTED_ERROR;

public class LineCommand extends AbstractCommand {

    private int x1;
    private int y1;
    private int x2;
    private int y2;

    private Pattern pattern = Pattern.compile("L\\s\\d{1,3}\\s\\d{1,3}\\s\\d{1,3}\\s\\d{1,3}");

    @Override
    public void init(CommandLineManager commandLineManager, List<String> parameters) {
        super.init(commandLineManager, parameters);
        Iterator<String> paramsIter = parameters.iterator();
        this.x1 = typeParser.parseInteger(paramsIter.next());
        this.y1 = typeParser.parseInteger(paramsIter.next());
        this.x2 = typeParser.parseInteger(paramsIter.next());
        this.y2 = typeParser.parseInteger(paramsIter.next());
        if (!ValidationUtil.isValidLine(x1, y1, x2, y2)) {
            throw new ValidationException(ONLY_HORIZONTAL_OR_VERTICAL_LINES_ARE_SUPPORTED_ERROR);
        }
    }

    @Override
    public void execute() {
        commandLineManager.getCurrentCanvas().line(x1, y1, x2, y2);
        commandLineManager.getCurrentCanvas().paint();
    }

    @Override
    public Pattern getPattern() {
        return pattern;
    }
}
