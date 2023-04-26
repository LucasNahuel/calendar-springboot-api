package com.lucas.calendarspringbootapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DuplicationError.class)
    protected ResponseEntity<Object> handleDuplicationError(DuplicationError ex,
                                                                     WebRequest request){


        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("value", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);

    }


    @ExceptionHandler(SavingError.class)
    public ResponseEntity<Object> handleSavingException(SavingError ex,
                                                        WebRequest request){


        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("value", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);

    }




}
