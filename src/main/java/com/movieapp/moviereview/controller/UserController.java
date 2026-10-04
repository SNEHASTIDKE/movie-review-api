package com.movieapp.moviereview.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movieapp.moviereview.config.JwtUtil;
import com.movieapp.moviereview.dto.LoginRequest;
import com.movieapp.moviereview.dto.LoginResponse;
import com.movieapp.moviereview.entity.User;
import com.movieapp.moviereview.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        return new ResponseEntity<>(userService.register(user), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        User user = userService.login(request.getEmail(), request.getPassword());
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return new LoginResponse(user.getId(), user.getName(), user.getEmail(), user.getRole().name(), token);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getById(id);
    }
}