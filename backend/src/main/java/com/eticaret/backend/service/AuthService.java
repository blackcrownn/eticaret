package com.eticaret.backend.service;

import com.eticaret.backend.dto.request.LoginRequest;
import com.eticaret.backend.dto.request.RegisterRequest;
import com.eticaret.backend.dto.response.AuthResponse;

/**
 * Service interface for Authentication operations.
 */
public interface AuthService {

    /**
     * Register a new user
     */
    AuthResponse register(RegisterRequest request);

    /**
     * Login user
     */
    AuthResponse login(LoginRequest request);
}
