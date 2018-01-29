package org.creditsuisse.canvas;

public class CanvasIOException extends RuntimeException {

    public CanvasIOException(String message) {
        super(message);
    }

    public CanvasIOException(String message, Throwable cause) {
        super(message, cause);
    }

    public CanvasIOException(Throwable cause) {
        super(cause);
    }

    public CanvasIOException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
