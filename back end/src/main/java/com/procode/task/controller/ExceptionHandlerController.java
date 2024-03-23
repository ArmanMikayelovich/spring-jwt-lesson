package com.procode.task.controller;

import com.procode.task.exceptions.*;
import com.procode.task.util.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundExceptions(NotFoundException exception) {
        return new ResponseEntity<>(
                new ApiError(exception.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ServerSideException.class)
    public ResponseEntity<ApiError> handleServerSideExceptions(ServerSideException exception) {
        return new ResponseEntity<>(
                new ApiError(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(RestrictedOperationException.class)
    public ResponseEntity<ApiError> handleRestrictedOperationExceptions(RestrictedOperationException exception) {
        return new ResponseEntity<>(
                new ApiError(exception.getMessage(), HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthenticationExceptions(AuthenticationException exception) {
        return new ResponseEntity<>(
                new ApiError(exception.getMessage(), HttpStatus.UNAUTHORIZED.value()),
                HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ApiError> handleAlreadyExistsExceptions(AlreadyExistsException exception) {
        return new ResponseEntity<>(
                new ApiError(exception.getMessage(), HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST);
    }
}