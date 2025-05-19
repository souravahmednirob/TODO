package com.nirob.todo.service.Impl;

import com.nirob.todo.model.dto.AuthRequestDto;
import com.nirob.todo.model.entity.User;
import com.nirob.todo.model.response.ApiResponse;
import com.nirob.todo.security.JwtTokenUtil;
import com.nirob.todo.service.AuthService;
import com.nirob.todo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public ApiResponse<String> register(AuthRequestDto registrationDto) {
        try {
            // Check if user exists
            if (userService.existsByMail(registrationDto.getMail())) {
                return ApiResponse.<String>error("Email already in use");
            }

            // Register the user
            User user = userService.registerUser(registrationDto);

            // Generate JWT token
            String token = jwtTokenUtil.generateToken(user.getMail());

            return ApiResponse.<String>success("User registered successfully", token);
        } catch (Exception e) {
            return ApiResponse.<String>error("Registration failed: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse<String> login(AuthRequestDto loginDto) {
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getMail(),
                            loginDto.getPassword()
                    )
            );

            // Set authentication in security context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate JWT token
            String token = jwtTokenUtil.generateToken(loginDto.getMail());

            return ApiResponse.<String>success("Login successful", token);
        } catch (Exception e) {
            return ApiResponse.<String>error("Authentication failed: " + e.getMessage());
        }
    }
}