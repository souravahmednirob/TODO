package com.nirob.todo.controller;

import com.nirob.todo.model.dto.AuthRequestDto;
import com.nirob.todo.model.response.ApiResponse;
import com.nirob.todo.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "API for user authentication")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Register a new user with email and password")
    public ResponseEntity<ApiResponse<String>> registerUser(@Valid @RequestBody AuthRequestDto registrationDto) {
        ApiResponse<String> response = authService.register(registrationDto);
        
        if (response.isSuccess()) {
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Login with email and password")
    public ResponseEntity<ApiResponse<String>> login(@Valid @RequestBody AuthRequestDto loginDto) {
        ApiResponse<String> response = authService.login(loginDto);
        
        if (response.isSuccess()) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }
}