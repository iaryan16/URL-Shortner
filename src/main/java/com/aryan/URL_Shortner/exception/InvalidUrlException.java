package com.aryan.URL_Shortner.exception;

public class InvalidUrlException extends RuntimeException{
    public InvalidUrlException(String message){
        super(message);
    }
}