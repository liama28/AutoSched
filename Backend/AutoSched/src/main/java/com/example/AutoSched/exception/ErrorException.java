package com.example.AutoSched.exception;

public class ErrorException extends RuntimeException {
    private static final long serialVersionUID = 5L;

    /**
     * String representation of an error with a
     */
    public String ErrorMessage;

    /**
     * Creates a error exception with a given error code and message
     * @param code
     * @param message
     */
    public ErrorException(int code, String message) {
        this.ErrorMessage = "{\"code\":" + String.valueOf(code) + ",\"message\":\"" + message + "\"}";
    }
}