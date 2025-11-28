package com.eticaret.backend.util;

import com.eticaret.backend.exception.ValidationException;

/**
 * Utility class for ID validation.
 */
public final class IdValidator {

    private IdValidator() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Validates that an ID is not null and is positive
     */
    public static void validateId(Long id, String resourceName) {
        if (id == null) {
            throw new ValidationException(resourceName + " ID cannot be null");
        }
        if (id <= 0) {
            throw new ValidationException(resourceName + " ID must be positive");
        }
    }

    /**
     * Validates that an ID is positive (allows null for optional cases)
     */
    public static void validateIdIfPresent(Long id, String resourceName) {
        if (id != null && id <= 0) {
            throw new ValidationException(resourceName + " ID must be positive");
        }
    }

    /**
     * Validates multiple IDs
     */
    public static void validateIds(Long[] ids, String resourceName) {
        if (ids == null || ids.length == 0) {
            throw new ValidationException(resourceName + " IDs cannot be null or empty");
        }
        for (Long id : ids) {
            validateId(id, resourceName);
        }
    }
}
