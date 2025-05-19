package com.nirob.todo.service.Impl;

import com.nirob.todo.exception.ResourceNotFoundException;
import com.nirob.todo.model.dto.AuthRequestDto;
import com.nirob.todo.model.entity.User;
import com.nirob.todo.repository.UserRepository;
import com.nirob.todo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(AuthRequestDto registrationDto) {
        // Check if user already exists
        if (existsByMail(registrationDto.getMail())) {
            throw new RuntimeException("Email already in use");
        }

        // Create new user
        User user = new User();
        user.setMail(registrationDto.getMail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        
        return userRepository.save(user);
    }

    @Override
    public User findByMail(String email) {
        return userRepository.findByMail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    @Override
    public boolean existsByMail(String email) {
        return userRepository.existsByMail(email);
    }
}