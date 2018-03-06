package org.creditsuisse;



import org.creditsuisse.canvas.CanvasIOException;
import org.creditsuisse.cli.CommandLineManager;
import org.creditsuisse.cli.command.CommandFactory;

import java.io.*;

import static org.creditsuisse.common.Constants.PRINT_STACK_TRACE_PROPERTY;

public class Main {

    public static void main(String [] args) throws IOException {
        try {
            CommandFactory commandFactory = new CommandFactory();
            CommandLineManager commandLineManager = new CommandLineManager(System.out, System.in, System.out);
            while (!commandLineManager.isDone()) {
                String commandString = commandLineManager.read();
                commandFactory.createCommand(commandLineManager, commandString).execute();
            }
        } catch (CanvasIOException e) {
            handleException(String.format("Error during canvas write operation : %s", e.getMessage()), e);
        } catch (Exception e) {
            handleException(String.format("Unexpected error : %s", e.getMessage()), e);
        }
    }

    public static void handleException(String message, Exception e) {
        System.out.println(message);
        if (Boolean.valueOf(System.getProperty(PRINT_STACK_TRACE_PROPERTY, "false"))) {
            e.printStackTrace();
        }
    }
}
