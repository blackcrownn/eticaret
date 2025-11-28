package com.eticaret.backend.service.impl;

import com.eticaret.backend.dto.response.UserResponse;
import com.eticaret.backend.exception.BusinessException;
import com.eticaret.backend.mapper.UserMapper;
import com.eticaret.backend.model.User;
import com.eticaret.backend.repository.UserRepository;
import com.eticaret.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of UserService.
 */
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserResponse getUserById(Long id) {
        log.debug("Fetching user with id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("User not found with id: " + id));

        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        log.debug("Fetching user with email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("User not found with email: " + email));

        return userMapper.toResponse(user);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        log.debug("Fetching all users");

        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateUserActiveStatus(Long id, boolean active) {
        log.info("Updating user active status for id: {} to {}", id, active);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("User not found with id: " + id));

        user.setActive(active);
        userRepository.save(user);

        log.info("User active status updated successfully");
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        log.info("Soft deleting user with id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("User not found with id: " + id));

        user.setActive(false);
        userRepository.save(user);

        log.info("User soft deleted successfully");
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
