package com.eticaret.backend.dto.response;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Standard error response structure for API errors.
 */
public class ErrorResponse {

    private boolean success = false;
    private String message;
    private String error;
    private List<String> details;
    private LocalDateTime timestamp;

    public ErrorResponse() {
    }

    public ErrorResponse(boolean success, String message, String error, List<String> details, LocalDateTime timestamp) {
        this.success = success;
        this.message = message;
        this.error = error;
        this.details = details;
        this.timestamp = timestamp;
    }

    public ErrorResponse(String message, String error) {
        this.success = false;
        this.message = message;
        this.error = error;
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(String message, String error, List<String> details) {
        this.success = false;
        this.message = message;
        this.error = error;
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
