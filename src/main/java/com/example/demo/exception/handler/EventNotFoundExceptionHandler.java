package com.example.demo.exception.handler;

import com.example.demo.exception.EventNotFoundException;
import com.example.demo.response.ResponseObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class EventNotFoundExceptionHandler {
    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<ResponseObject> handler(EventNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseObject(HttpStatus.NOT_FOUND.value(), ex.getMessage(), null));
    }
}
