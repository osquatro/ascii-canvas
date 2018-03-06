package org.creditsuisse.cli;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
