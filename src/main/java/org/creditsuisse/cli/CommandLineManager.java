package org.creditsuisse.cli;

import org.creditsuisse.canvas.AsciiCanvas;

import java.io.*;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.creditsuisse.common.Constants.MESSAGES_FILE;

public class CommandLineManager {

    private BufferedReader commandReader;
    private PrintStream outputWriter;
    private BufferedWriter canvasWriter;
    private ResourceBundle messages = ResourceBundle.getBundle(MESSAGES_FILE, Locale.US);

    private AsciiCanvas currentCanvas = null;

    private boolean isDone = false;

    public CommandLineManager(PrintStream outputWriter, InputStream cmdReader, PrintStream canvasWriterStream) {
        this.commandReader = new BufferedReader(new InputStreamReader(cmdReader));
        this.outputWriter = outputWriter;
        this.canvasWriter = new BufferedWriter(new OutputStreamWriter(canvasWriterStream));
    }

    public String read() throws IOException {
        outputWriter.print(">");
        return commandReader.readLine();
    }

    public AsciiCanvas getCurrentCanvas() {
        return currentCanvas;
    }

    public void printMessage(String errorCode) {
        outputWriter.println(messages.getString(errorCode));
    }

    public void initCanvas(int width, int height) {
        currentCanvas = new AsciiCanvas(canvasWriter, width, height);
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}

