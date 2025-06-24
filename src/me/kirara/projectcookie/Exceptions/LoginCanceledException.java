package me.kirara.projectcookie.Exceptions;

public class LoginCanceledException extends RuntimeException {
    public LoginCanceledException(String message) {
        super(message);
    }
}
