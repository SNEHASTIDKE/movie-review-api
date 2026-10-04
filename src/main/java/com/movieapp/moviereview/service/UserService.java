package com.movieapp.moviereview.service;

import com.movieapp.moviereview.entity.Role;
import com.movieapp.moviereview.entity.User;
import com.movieapp.moviereview.exception.BadRequestException;
import com.movieapp.moviereview.exception.ResourceNotFoundException;
import com.movieapp.moviereview.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new BadRequestException("Email already registered");
        }
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
    }
    
    public User login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new BadRequestException("Invalid email or password");
        }

        return user;
    }
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email " + email));
    }
}