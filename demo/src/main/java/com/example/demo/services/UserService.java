package com.example.demo.services;

import org.springframework.stereotype.Service;

import com.example.demo.dtos.LoginRequestDto;
import com.example.demo.dtos.LoginResponseDto;
import com.example.demo.entities.User;
import com.example.demo.repositories.UserJpaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
    private final JwtService jwtService;
    private final UserJpaRepository userJpaRepository;

    public LoginResponseDto login(LoginRequestDto request) throws Exception {
        // Find the user by email (username)
        User user = this.userJpaRepository.findByUsername(request.getUsername())
                .orElseThrow(
                        () -> new Exception("User not found with username: " + request.getUsername()));

        // Verify password
        if (!request.getPassword().equals(user.getPassword())) {
            throw new Exception("Invalid username or password");
        }

        // Generate a new access token (with full data + roles)
        String accessToken = jwtService.generateAccessToken(user);

        return LoginResponseDto.builder()
                .username(user.getUsername())
                .accessToken(accessToken)
                .build();
    }
}
