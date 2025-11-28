package com.eticaret.backend.util;

import com.eticaret.backend.exception.ValidationException;

/**
 * Utility class for String validation.
 */
public final class StringValidator {

    private StringValidator() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Validates that a string is not null or blank
     */
    public static void validateNotBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(fieldName + " cannot be null or blank");
        }
    }

    /**
     * Validates string length
     */
    public static void validateLength(String value, String fieldName, int minLength, int maxLength) {
        if (value != null) {
            int length = value.length();
            if (length < minLength || length > maxLength) {
                throw new ValidationException(
                        fieldName + " length must be between " + minLength + " and " + maxLength + " characters");
            }
        }
    }

    /**
     * Validates email format
     */
    public static void validateEmail(String email) {
        if (email != null && !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ValidationException("Invalid email format");
        }
    }

    /**
     * Validates that string contains only alphanumeric characters
     */
    public static void validateAlphanumeric(String value, String fieldName) {
        if (value != null && !value.matches("^[a-zA-Z0-9]+$")) {
            throw new ValidationException(fieldName + " must contain only alphanumeric characters");
        }
    }
}
