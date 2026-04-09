package com.example.reviews.utility;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {
        Map<String, String> validationErrors = new LinkedHashMap<>();
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        logger.warn("Validation failed path={} errors={}", request.getRequestURI(), validationErrors);

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Validation Failed",
                "Request validation failed",
                request.getRequestURI(),
                validationErrors
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(
            ConstraintViolationException exception,
            HttpServletRequest request) {
        Map<String, String> validationErrors = new LinkedHashMap<>();
        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            validationErrors.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        logger.warn("Constraint validation failed path={} errors={}", request.getRequestURI(), validationErrors);

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Validation Failed",
                "Constraint validation failed",
                request.getRequestURI(),
                validationErrors
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException exception,
            HttpServletRequest request) {
        logger.warn("Malformed request body path={} message={}", request.getRequestURI(), exception.getMessage());
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Bad Request",
                "Request body is missing, malformed, or contains invalid values",
                request.getRequestURI(),
                null
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException exception,
            HttpServletRequest request) {
        String message = "Invalid value for '" + exception.getName() + "'";
        logger.warn("Type mismatch path={} parameter={} value={}",
                request.getRequestURI(), exception.getName(), exception.getValue());
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Bad Request",
                message,
                request.getRequestURI(),
                null
        );
    }

    @ExceptionHandler({ReviewNotFound.class, userNotFoundException.class, EmptyResultDataAccessException.class})
    public ResponseEntity<ApiErrorResponse> handleNotFound(
            Exception exception,
            HttpServletRequest request) {
        String message = exception instanceof EmptyResultDataAccessException
                ? "Review not found"
                : exception.getMessage();
        logger.warn("Not found path={} message={}", request.getRequestURI(), message);

        return buildResponse(
                HttpStatus.NOT_FOUND,
                "Not Found",
                message,
                request.getRequestURI(),
                null
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException exception,
            HttpServletRequest request) {
        String message = "Database constraint violation";
        if (exception.getMostSpecificCause() != null &&
                exception.getMostSpecificCause().getMessage() != null &&
                exception.getMostSpecificCause().getMessage().toLowerCase().contains("booking_id")) {
            message = "A review already exists for this booking";
        }
        logger.error("Data integrity violation path={} message={}", request.getRequestURI(), message, exception);

        return buildResponse(
                HttpStatus.CONFLICT,
                "Conflict",
                message,
                request.getRequestURI(),
                null
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(
            Exception exception,
            HttpServletRequest request) {
        logger.error("Unhandled exception path={} message={}", request.getRequestURI(), exception.getMessage(), exception);
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                exception.getMessage() == null ? "Unexpected error occurred" : exception.getMessage(),
                request.getRequestURI(),
                null
        );
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(
            HttpStatus status,
            String error,
            String message,
            String path,
            Map<String, String> validationErrors) {
        ApiErrorResponse response = new ApiErrorResponse(
                status.value(),
                error,
                message,
                path,
                validationErrors
        );
        return ResponseEntity.status(status).body(response);
    }
}
