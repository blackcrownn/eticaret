package com.eticaret.backend.service;

import com.eticaret.backend.dto.response.UserResponse;

import java.util.List;

/**
 * Service interface for User operations.
 */
public interface UserService {

    /**
     * Get user by id
     */
    UserResponse getUserById(Long id);

    /**
     * Get user by email
     */
    UserResponse getUserByEmail(String email);

    /**
     * Get all users
     */
    List<UserResponse> getAllUsers();

    /**
     * Update user active status
     */
    void updateUserActiveStatus(Long id, boolean active);

    /**
     * Delete user (soft delete)
     */
    void deleteUser(Long id);

    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);
}
