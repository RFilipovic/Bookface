package com.bookface.comms.exception;

public class ApiRequestException extends RuntimeException{
    public ApiRequestException(String message){
        super(message);
    }
}
