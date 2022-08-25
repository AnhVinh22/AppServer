package com.example.demo.exception.handler;

import com.example.demo.exception.CommentNotFoundException;
import com.example.demo.response.ResponseObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommentNotFoundExceptionHandler {
    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ResponseObject> handler(CommentNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseObject(HttpStatus.NOT_FOUND.value(), ex.getMessage(), null));
    }
}
