package com.lucas.calendarspringbootapi.exception;

public class SavingError extends RuntimeException{

    public SavingError(String elementName){
        super("There was an error saving the "+elementName);
    }
}
