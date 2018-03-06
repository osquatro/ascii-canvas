package org.creditsuisse.cli.command;

import org.creditsuisse.cli.CommandLineManager;
import org.creditsuisse.cli.ValidationException;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import static org.creditsuisse.common.Constants.CANVAS_SIZE_NOT_VALID_ERROR;
import static org.creditsuisse.common.ValidationUtil.isValidCanvasSize;

public class CanvasCommand extends AbstractCommand {
    private int width;
    private int height;

    private Pattern pattern = Pattern.compile("C\\s\\d{1,3}\\s\\d{1,3}");

    @Override
    public void init(CommandLineManager commandLineManager, List<String> parameters) {
        super.init(commandLineManager, parameters);
        Iterator<String> paramsIter = parameters.iterator();
        this.width = typeParser.parseInteger(paramsIter.next());
        this.height = typeParser.parseInteger(paramsIter.next());
        if (!isValidCanvasSize(width, height)) {
            throw new ValidationException(CANVAS_SIZE_NOT_VALID_ERROR);
        }
    }

    @Override
    public void execute() {
        commandLineManager.initCanvas(width, height);
        commandLineManager.getCurrentCanvas().paint();
    }

    @Override
    public Pattern getPattern() {
        return pattern;
    }
}
