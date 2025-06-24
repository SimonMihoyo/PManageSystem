package me.kirara.projectcookie.Exceptions;

public class CloseConnectionErrorException extends RuntimeException {
    public CloseConnectionErrorException(String message) {
        super(message);
    }
}
