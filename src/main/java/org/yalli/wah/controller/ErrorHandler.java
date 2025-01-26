package org.yalli.wah.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.yalli.wah.model.exception.ExcessivePasswordResetAttemptsException;
import org.yalli.wah.model.dto.ExceptionResponse;
import org.yalli.wah.model.exception.InvalidInputException;
import org.yalli.wah.model.exception.InvalidOtpException;
import org.yalli.wah.model.exception.PermissionException;
import org.yalli.wah.model.exception.ResourceNotFoundException;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(InvalidInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handle(InvalidInputException e) {
        return new ExceptionResponse(e.getMessage());
    }

    @ExceptionHandler(InvalidOtpException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionResponse handle(InvalidOtpException e) {
        return new ExceptionResponse(e.getMessage());
    }

    @ExceptionHandler(PermissionException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionResponse handle(PermissionException e) {
        return new ExceptionResponse(e.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handle(ResourceNotFoundException e) {
        return new ExceptionResponse(e.getMessage());
    }

    @ExceptionHandler(ExcessivePasswordResetAttemptsException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ExceptionResponse handleExcessivePasswordResetAttempts(ExcessivePasswordResetAttemptsException e) {
        return new ExceptionResponse(e.getMessage());
    }
}
