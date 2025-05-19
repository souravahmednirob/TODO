package com.nirob.todo.service;

import com.nirob.todo.model.dto.AuthRequestDto;
import com.nirob.todo.model.entity.User;

public interface UserService {
    User registerUser(AuthRequestDto registrationDto);
    User findByMail(String email);
    boolean existsByMail(String email);
}