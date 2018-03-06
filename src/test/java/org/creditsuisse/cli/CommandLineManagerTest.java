package org.creditsuisse.cli;

import org.creditsuisse.canvas.AsciiCanvas;
import org.creditsuisse.cli.command.CommandFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.creditsuisse.common.Constants.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class CommandLineManagerTest {

    static final ResourceBundle messages = ResourceBundle.getBundle(MESSAGES_FILE, Locale.US);

    static final String NOT_VALID_ARGUMENTS_MESSAGE = String.format("%s\n", messages.getString(COMMAND_ARGUMENTS_MALFORMED));
    static final String NOT_VALID_CANVAS_SIZE_MESSAGE = String.format("%s\n", messages.getString(CANVAS_SIZE_NOT_VALID_ERROR));
    static final String NOT_SUPPORTED_LINE_MESSAGE = String.format("%s\n", messages.getString(ONLY_HORIZONTAL_OR_VERTICAL_LINES_ARE_SUPPORTED_ERROR));
    static final String NOT_CREATED_CANVAS_MESSAGE = String.format("%s\n", messages.getString(CANVAS_NOT_CREATED_ERROR));

    PrintStream printStream;
    ByteArrayOutputStream outContent;

    CommandFactory commandFactory = new CommandFactory();
    CommandLineManager commandLineManagerMock;
    AsciiCanvas asciiCanvas;

    @Before
    public void init() {
        asciiCanvas = mock(AsciiCanvas.class);
        outContent = new ByteArrayOutputStream();
        printStream = new PrintStream(outContent);
        commandLineManagerMock = new CommandLineManager(printStream, System.in, printStream) {
            @Override
            public AsciiCanvas getCurrentCanvas() {
                return asciiCanvas;
            }
        };
    }

    @Test
    public void testQuitCommand() {
        commandFactory.createCommand(commandLineManagerMock, "Q").execute();
        assertTrue(commandLineManagerMock.isDone());
    }

    @Test
    public void testCanvasCommandValid() {
        CommandLineManager commandLineManager = new CommandLineManager(printStream, System.in, printStream);
        commandFactory.createCommand(commandLineManager, "C 10 10").execute();
        assertNotNull(commandLineManager.getCurrentCanvas());
    }

    @Test
    public void testCanvasSizeNonValid() {
        CommandLineManager commandLineManager = new CommandLineManager(printStream, System.in, System.out);
        commandFactory.createCommand(commandLineManager, "C 101 101").execute();
        assertOutput(NOT_VALID_CANVAS_SIZE_MESSAGE);
    }

    @Test
    public void testCanvasArgumentsNonValid() {
        CommandLineManager commandLineManager = new CommandLineManager(printStream, System.in, System.out);
        commandFactory.createCommand(commandLineManager, "C 13331 133331").execute();
        assertOutput(NOT_VALID_ARGUMENTS_MESSAGE);
    }

    @Test
    public void testLineCommandBeforeCanvas() {
        CommandLineManager commandLineManager = new CommandLineManager(printStream, System.in, printStream);
        commandFactory.createCommand(commandLineManager, "L 10 5 10 20").execute();
        assertOutput(NOT_CREATED_CANVAS_MESSAGE);
    }

    @Test
    public void testLineCommand() {
        commandFactory.createCommand(commandLineManagerMock, "L 10 5 10 20").execute();
        verify(asciiCanvas, times(1)).line(eq(10), eq(5), eq(10), eq(20));
        verify(asciiCanvas, times(1)).paint();
    }

    @Test
    public void testLineCommandNotValidArguments() {
        commandFactory.createCommand(commandLineManagerMock, "L 10234 5 10 20").execute();
        assertOutput(NOT_VALID_ARGUMENTS_MESSAGE);
    }

    @Test
    public void testLineCommandNotSupported() {
        commandFactory.createCommand(commandLineManagerMock, "L 10 10 20 20").execute();
        assertOutput(NOT_SUPPORTED_LINE_MESSAGE);
    }

    @Test
    public void testFillCommand() {
        commandFactory.createCommand(commandLineManagerMock, "B 10 5 *").execute();
        verify(asciiCanvas, times(1)).fill(eq(10), eq(5), eq((int)'*'));
        verify(asciiCanvas, times(1)).paint();
    }

    @Test
    public void testFillCommandNotValidArgs() {
        commandFactory.createCommand(commandLineManagerMock, "B *").execute();
        assertOutput(NOT_VALID_ARGUMENTS_MESSAGE);
    }

    @Test
    public void testRectCommand() {
        commandFactory.createCommand(commandLineManagerMock, "R 10 5 20 30").execute();
        verify(asciiCanvas, times(1)).rectangle(eq(10), eq(5), eq(20), eq(30));
        verify(asciiCanvas, times(1)).paint();
    }

    @Test
    public void testRectCommandNotValidArgs() {
        commandFactory.createCommand(commandLineManagerMock, "R 1044 5 20 430").execute();
        assertOutput(NOT_VALID_ARGUMENTS_MESSAGE);
    }

    private void assertOutput(String expected) {
        try {
            String out = new String(outContent.toByteArray(), "UTF-8");
            assertEquals(expected, out);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Encoding not supported.", e);
        }
    }
}
