package com.lucas.calendarspringbootapi.exception;

public class DuplicationError extends RuntimeException{

    public DuplicationError(String element){
        super(element + " is already in use");
    }
}
