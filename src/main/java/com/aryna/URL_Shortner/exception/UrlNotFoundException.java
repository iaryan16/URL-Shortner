package com.aryna.URL_Shortner.exception;

public class UrlNotFoundException extends RuntimeException{
    public UrlNotFoundException(String message){
        super(message);
    }
}