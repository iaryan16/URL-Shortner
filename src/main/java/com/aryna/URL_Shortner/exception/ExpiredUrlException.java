package com.aryna.URL_Shortner.exception;

public class ExpiredUrlException extends RuntimeException{
    public ExpiredUrlException(String message) {
        super(message);
    }
}
