package com.eticaret.backend.exception;

/**
 * Exception thrown when a requested resource is not found.
 */
public class NotFoundException extends BusinessException {

    public NotFoundException(String message) {
        super(message, "NOT_FOUND");
    }

    public NotFoundException(String resourceName, Long id) {
        super(String.format("%s with id %d not found", resourceName, id), "NOT_FOUND");
    }

    public NotFoundException(String resourceName, String identifier) {
        super(String.format("%s with identifier '%s' not found", resourceName, identifier), "NOT_FOUND");
    }
}
