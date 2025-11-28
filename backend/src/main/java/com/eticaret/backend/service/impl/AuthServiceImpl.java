package com.eticaret.backend.service.impl;

import com.eticaret.backend.dto.request.LoginRequest;
import com.eticaret.backend.dto.request.RegisterRequest;
import com.eticaret.backend.dto.response.AuthResponse;
import com.eticaret.backend.dto.response.UserResponse;
import com.eticaret.backend.exception.BusinessException;
import com.eticaret.backend.mapper.UserMapper;
import com.eticaret.backend.model.User;
import com.eticaret.backend.repository.UserRepository;
import com.eticaret.backend.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementation of AuthService.
 */
@Service
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email already registered: " + request.getEmail());
        }

        // Create user entity
        User user = userMapper.toEntity(request);

        // Encode password
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Save user
        User savedUser = userRepository.save(user);
        log.info("User registered successfully with id: {}", savedUser.getId());

        // Generate token (simplified - in production use JWT)
        String token = generateToken(savedUser);

        UserResponse userResponse = userMapper.toResponse(savedUser);
        return new AuthResponse(token, userResponse, "User registered successfully");
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        log.info("User login attempt for email: {}", request.getEmail());

        // Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException("Invalid email or password"));

        // Check if user is active
        if (!user.getActive()) {
            throw new BusinessException("User account is deactivated");
        }

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("Invalid email or password");
        }

        log.info("User logged in successfully: {}", request.getEmail());

        // Generate token (simplified - in production use JWT)
        String token = generateToken(user);

        UserResponse userResponse = userMapper.toResponse(user);
        return new AuthResponse(token, userResponse, "Login successful");
    }

    /**
     * Generate authentication token
     * Simplified implementation - in production, use JWT with proper secret and
     * expiration
     */
    private String generateToken(User user) {
        return "Bearer-" + UUID.randomUUID().toString() + "-" + user.getId();
    }
}
