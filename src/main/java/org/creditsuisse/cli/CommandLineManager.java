package org.creditsuisse.cli;

import org.creditsuisse.canvas.AsciiCanvas;
import org.creditsuisse.common.ValidationUtil;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

import static org.creditsuisse.common.Constants.*;
import static org.creditsuisse.common.ValidationUtil.isValidCanvasSize;

public class CommandLineManager {

    private BufferedReader commandReader;
    private PrintStream outputWriter;
    private BufferedWriter canvasWriter;

    private AsciiCanvas currentCanvas = null;
    private ResourceBundle messages = ResourceBundle.getBundle(MESSAGES_FILE, Locale.US);

    private static Map<Command, Pattern> COMMANDS_MAP = Collections.unmodifiableMap(new HashMap<Command, Pattern>() {{
        put(Command.CANVAS, Pattern.compile("C\\s\\d+\\s\\d+"));
        put(Command.LINE, Pattern.compile("L\\s\\d+\\s\\d+\\s\\d+\\s+\\d+"));
        put(Command.RECTANGLE, Pattern.compile("R\\s\\d+\\s\\d+\\s\\d+\\s+\\d+"));
        put(Command.FILL, Pattern.compile("B\\s\\d+\\s\\d+\\s[!-~]{1}"));
        put(Command.QUIT, Pattern.compile("Q"));
    }});


    public CommandLineManager(PrintStream outputWriter, InputStream cmdReader, PrintStream canvasWriterStream) {
        this.commandReader = new BufferedReader(new InputStreamReader(cmdReader));
        this.outputWriter = outputWriter;
        this.canvasWriter = new BufferedWriter(new OutputStreamWriter(canvasWriterStream));
    }

    public void consume() {
        try {
            Command command = null;
            do {
                String commandString = read();
                Optional<Command> optionalCommand = getCommand(commandString);

                if (optionalCommand.isPresent()) {
                    String[] params = commandString.split(COMMAND_DELIMITER);
                    command = optionalCommand.get();
                    if (Command.CANVAS != command && Command.QUIT != command && currentCanvas == null) {
                        outputWriter.println(messages.getString(CANVAS_NOT_CREATED_ERROR));
                    } else if (Command.CANVAS == command) {
                        createCanvas(params);
                    } else if (Command.LINE == command) {
                        paintLine(params);
                    } else if (Command.RECTANGLE == command) {
                        paintRectangle(params);
                    } else if (Command.FILL == command) {
                        paintFill(params);
                    }
                } else {
                    outputWriter.println(messages.getString(UNKNOWN_OR_MALFORMED_COMMAND_ERROR));
                }
            } while (Command.QUIT != command);
        } catch (IOException io) {
            throw new RuntimeException("IO error:", io);
        }
    }


    private Optional<Command> getCommand(String commandString) {
        if (commandString != null && commandString.length() > 0) {
            Command command = Command.fromString(String.valueOf(commandString.charAt(0)));
            if (command != null)
                if (COMMANDS_MAP.get(command).matcher(commandString).matches())
                    return Optional.of(command);
        }
        return Optional.empty();
    }

    private String read() throws IOException {
        outputWriter.print(">");
        return commandReader.readLine();
    }

    private void createCanvas(String[] params) {
        int width = Integer.valueOf(params[1]);
        int height = Integer.valueOf(params[2]);
        if (isValidCanvasSize(width, height)) {
            currentCanvas = new AsciiCanvas(canvasWriter, width, height);
            currentCanvas.paint();
        } else {
            outputWriter.println(messages.getString(CANVAS_SIZE_NOT_VALID_ERROR));
        }
    }

    private void paintLine(String[] params) {
        int x1 = Integer.valueOf(params[1]);
        int y1 = Integer.valueOf(params[2]);
        int x2 = Integer.valueOf(params[3]);
        int y2 = Integer.valueOf(params[4]);
        if (ValidationUtil.isValidLine(x1, y1, x2, y2)) {
            currentCanvas.line(x1, y1, x2, y2);
            currentCanvas.paint();
        } else {
            outputWriter.println(messages.getString(ONLY_HORIZONTAL_OR_VERTICAL_LINES_ARE_SUPPORTED_ERROR));
        }
    }

    private void paintRectangle(String[] params) {
        int x1 = Integer.valueOf(params[1]);
        int y1 = Integer.valueOf(params[2]);
        int x2 = Integer.valueOf(params[3]);
        int y2 = Integer.valueOf(params[4]);

        currentCanvas.rectangle(x1, y1, x2, y2);
        currentCanvas.paint();
    }

    private void paintFill(String[] params) {
        int x = Integer.valueOf(params[1]);
        int y = Integer.valueOf(params[2]);
        char color = params[3].charAt(0);
        currentCanvas.fill(x, y, color);
        currentCanvas.paint();
    }
}

