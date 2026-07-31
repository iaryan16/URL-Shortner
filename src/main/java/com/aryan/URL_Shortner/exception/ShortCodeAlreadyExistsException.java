package com.aryan.URL_Shortner.exception;

public class ShortCodeAlreadyExistsException extends RuntimeException {
    public ShortCodeAlreadyExistsException(String message) {
        super(message);
    }
}