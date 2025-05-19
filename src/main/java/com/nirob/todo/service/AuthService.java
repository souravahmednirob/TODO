package com.nirob.todo.service;

import com.nirob.todo.model.dto.AuthRequestDto;
import com.nirob.todo.model.response.ApiResponse;

public interface AuthService {
    ApiResponse<String> register(AuthRequestDto registrationDto);
    ApiResponse<String> login(AuthRequestDto loginDto);
}