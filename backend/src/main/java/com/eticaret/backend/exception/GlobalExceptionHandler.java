package com.eticaret.backend.exception;

import com.eticaret.backend.dto.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Global exception handler for the entire application.
 * Catches and formats all exceptions into consistent ErrorResponse objects.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

        private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

        /**
         * Handle NotFoundException
         */
        @ExceptionHandler(NotFoundException.class)
        public ResponseEntity<ErrorResponse> handleNotFoundException(
                        NotFoundException ex, WebRequest request) {
                log.error("NotFoundException: {}", ex.getMessage());

                ErrorResponse errorResponse = new ErrorResponse(
                                ex.getMessage(),
                                ex.getErrorCode());

                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(errorResponse);
        }

        /**
         * Handle ValidationException
         */
        @ExceptionHandler(ValidationException.class)
        public ResponseEntity<ErrorResponse> handleValidationException(
                        ValidationException ex, WebRequest request) {
                log.error("ValidationException: {}", ex.getMessage());

                ErrorResponse errorResponse = new ErrorResponse(
                                ex.getMessage(),
                                ex.getErrorCode(),
                                ex.getValidationErrors());

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(errorResponse);
        }

        /**
         * Handle Spring Validation Errors
         */
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
                        MethodArgumentNotValidException ex, WebRequest request) {
                log.error("MethodArgumentNotValidException: {}", ex.getMessage());

                List<String> errors = new ArrayList<>();
                for (FieldError error : ex.getBindingResult().getFieldErrors()) {
                        errors.add(error.getField() + ": " + error.getDefaultMessage());
                }

                ErrorResponse errorResponse = new ErrorResponse(
                                "Validation failed",
                                "VALIDATION_ERROR",
                                errors);

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(errorResponse);
        }

        /**
         * Handle BusinessException
         */
        @ExceptionHandler(BusinessException.class)
        public ResponseEntity<ErrorResponse> handleBusinessException(
                        BusinessException ex, WebRequest request) {
                log.error("BusinessException: {}", ex.getMessage());

                ErrorResponse errorResponse = new ErrorResponse(
                                ex.getMessage(),
                                ex.getErrorCode());

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(errorResponse);
        }

        /**
         * Handle all other exceptions
         */
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGlobalException(
                        Exception ex, WebRequest request) {
                log.error("Unexpected error occurred", ex);

                ErrorResponse errorResponse = new ErrorResponse(
                                "An unexpected error occurred",
                                "INTERNAL_SERVER_ERROR");

                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(errorResponse);
        }
}
