package com.example.demo.exception;

public class UserTypeNotAllow extends RuntimeException {
    public UserTypeNotAllow(String message) {
        super(message);
    }
}
