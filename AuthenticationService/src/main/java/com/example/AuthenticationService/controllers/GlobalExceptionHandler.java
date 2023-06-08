package com.example.AuthenticationService.controllers;

import com.example.AuthenticationService.exception.InvalidCredentialsException;
import com.example.AuthenticationService.exception.UserEmailExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({UserEmailExistsException.class, InvalidCredentialsException.class})
    public ResponseEntity<Object> responseNotFoundException (Exception exception) {
        if (exception instanceof UserEmailExistsException) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.ALREADY_REPORTED);
        } else {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }

    }
}
