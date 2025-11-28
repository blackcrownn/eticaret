package com.eticaret.backend.exception;

import java.util.List;

/**
 * Exception thrown when validation fails.
 */
public class ValidationException extends BusinessException {

    private final List<String> validationErrors;

    public ValidationException(String message) {
        super(message, "VALIDATION_ERROR");
        this.validationErrors = List.of(message);
    }

    public ValidationException(List<String> validationErrors) {
        super("Validation failed", "VALIDATION_ERROR");
        this.validationErrors = validationErrors;
    }

    public ValidationException(String message, List<String> validationErrors) {
        super(message, "VALIDATION_ERROR");
        this.validationErrors = validationErrors;
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }
}
