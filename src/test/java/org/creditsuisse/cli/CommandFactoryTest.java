package org.creditsuisse.cli;

import org.creditsuisse.canvas.AsciiCanvas;
import org.creditsuisse.cli.command.*;
import org.junit.Before;
import org.junit.Test;

import static org.creditsuisse.common.Constants.*;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class CommandFactoryTest {

    CommandLineManager commandLineManager;
    CommandFactory commandFactory = new CommandFactory();
    AsciiCanvas asciiCanvas;

    @Before
    public void init() {
        commandLineManager = mock(CommandLineManager.class);
        asciiCanvas = mock(AsciiCanvas.class);
        when(commandLineManager.getCurrentCanvas()).thenReturn(asciiCanvas);
    }

    @Test
    public void createValidCanvas() {
        AbstractCommand command = commandFactory.createCommand(commandLineManager, "C 10 10");
        assertTrue(command instanceof CanvasCommand);
        command.execute();
        verify(commandLineManager, times(1)).initCanvas(eq(10), eq(10));
    }

    @Test
    public void createCanvasNotValid() {
        AbstractCommand command = commandFactory.createCommand(commandLineManager, "C 0");
        assertMalformedCommand(command, COMMAND_ARGUMENTS_MALFORMED);
    }

    @Test
    public void createCanvasWrongSize() {
        AbstractCommand command = commandFactory.createCommand(commandLineManager, "C 130 120");
        assertMalformedCommand(command, CANVAS_SIZE_NOT_VALID_ERROR);
    }

    @Test
    public void createValidLineBeforeCanvas() {
        when(commandLineManager.getCurrentCanvas()).thenReturn(null);
        AbstractCommand command = commandFactory.createCommand(commandLineManager, "L 10 10 10 20");
        assertMalformedCommand(command, CANVAS_NOT_CREATED_ERROR);
    }

    @Test
    public void createValidLine() {
        AbstractCommand command = commandFactory.createCommand(commandLineManager, "L 10 10 10 20");
        assertTrue(command instanceof LineCommand);
        command.execute();
        verify(asciiCanvas, times(1)).line(eq(10), eq(10),eq(10), eq(20));
    }

    @Test
    public void createLineNotValid() {
        AbstractCommand command = commandFactory.createCommand(commandLineManager, "L 10 asdf");
        assertMalformedCommand(command, COMMAND_ARGUMENTS_MALFORMED);
    }

    @Test
    public void createNotSupportedLine() {
        AbstractCommand command = commandFactory.createCommand(commandLineManager, "L 10 10 20 20");
        assertMalformedCommand(command, ONLY_HORIZONTAL_OR_VERTICAL_LINES_ARE_SUPPORTED_ERROR);
    }

    @Test
    public void createValidQuit() {
        AbstractCommand command = commandFactory.createCommand(commandLineManager, "Q");
        assertTrue(command instanceof QuitCommand);
        command.execute();
        verify(commandLineManager, times(1)).setDone(eq(true));
    }

    @Test
    public void createNotValidQuit() {
        AbstractCommand command = commandFactory.createCommand(commandLineManager, "Q s");
        assertMalformedCommand(command, COMMAND_ARGUMENTS_MALFORMED);
    }

    @Test
    public void createValidRect() {
        AbstractCommand command = commandFactory.createCommand(commandLineManager, "R 10 10 20 20");
        assertTrue(command instanceof RectangleCommand);
        command.execute();
        verify(asciiCanvas, times(1)).rectangle(eq(10), eq(10),eq(20), eq(20));
    }

    @Test
    public void createNotValidRect() {
        AbstractCommand command = commandFactory.createCommand(commandLineManager, "R s");
        assertMalformedCommand(command, COMMAND_ARGUMENTS_MALFORMED);
    }

    private void assertMalformedCommand(AbstractCommand command, String message) {
        assertTrue(command instanceof MalformedCommand);
        command.execute();
        verify(commandLineManager, times(1)).printMessage(eq(message));
    }
}
