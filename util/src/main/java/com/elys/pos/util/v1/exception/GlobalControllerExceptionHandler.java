package com.elys.pos.util.v1.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public @ResponseBody HttpErrorInfo handleBadRequestException(ServerHttpRequest request, BadRequestException ex) {
        return creatHttpErrorInfo(BAD_REQUEST, request, ex.getMessage());
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public @ResponseBody HttpErrorInfo handleNotFoundException(ServerHttpRequest request, NotFoundException ex) {
        return creatHttpErrorInfo(NOT_FOUND, request, ex.getMessage());
    }

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidInputException.class)
    public @ResponseBody HttpErrorInfo handleInvalidInputException(ServerHttpRequest request, InvalidInputException ex) {
        return creatHttpErrorInfo(UNPROCESSABLE_ENTITY, request, ex.getMessage());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(WebExchangeBindException.class)
    public @ResponseBody HttpErrorInfo handleValidationExceptions(ServerHttpRequest request, WebExchangeBindException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Validation error").toList();
        String message = String.join("; ", errors);
        return creatHttpErrorInfo(BAD_REQUEST, request, message);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public @ResponseBody HttpErrorInfo handleConstraintViolationExceptions(ServerHttpRequest request, ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).toList();
        String message = String.join("; ", errors);
        return creatHttpErrorInfo(BAD_REQUEST, request, message);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public @ResponseBody HttpErrorInfo handleDataIntegrityViolationException(ServerHttpRequest request, DataIntegrityViolationException ex) {
        String message = "A database constraint was violated.";

        Throwable cause = ex.getCause();
        if (cause instanceof org.hibernate.exception.ConstraintViolationException) {
            String constraintName = ((org.hibernate.exception.ConstraintViolationException) cause).getConstraintName();
            message = getCustomMessageForConstraint(constraintName);
        }

        return creatHttpErrorInfo(BAD_REQUEST, request, message);
    }

    private String getCustomMessageForConstraint(String constraintName) {
        if ("unique_item_name".equals(constraintName)) {
            return "The item name is already taken. Please choose a different one.";
        } else if ("unique_email".equals(constraintName)) {
            return "The email address is already registered.";
        } else if ("unique_category_name".equals(constraintName)) {
            return "The category name is already taken. Please choose a different one.";
        }
        return "A database constraint violation occurred.";
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public HttpErrorInfo handleGenericException(ServerHttpRequest request, Throwable ex) {
        return creatHttpErrorInfo(INTERNAL_SERVER_ERROR, request, ex.getMessage());
    }

    private HttpErrorInfo creatHttpErrorInfo(HttpStatus httpStatus, ServerHttpRequest request, String message) {
        final String path = request.getPath().pathWithinApplication().value();
        log.debug("Returning HTTP status: {} for path: {}, message: {}", httpStatus, path, message);
        return new HttpErrorInfo(httpStatus, path, message);
    }
}
