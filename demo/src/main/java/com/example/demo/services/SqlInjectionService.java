package com.example.demo.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.LoginRequestDto;
import com.example.demo.dtos.LoginResponseDto;
import com.example.demo.entities.User;
import com.example.demo.exceptions.HttpException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SqlInjectionService {
    private final JwtService jwtService;
    private final EntityManager entityManager;

    public LoginResponseDto login(LoginRequestDto request) {
        try {

            // For SqlInjection demo.
            // This is a vulnerable code that allows SQL Injection.
            String query = "SELECT * FROM users WHERE username = '" + request.getUsername() + "' AND password = '"
                    + request.getPassword() + "'";

            System.out.println("Executing query: " + query);

            // Note: This is a bad practice and should not be used in production code.
            /**
             * Example of a SQL Injection attack:
             * If the username is:
             * {
             * "username": "tungnt@softech.vn' OR '1' = '2",
             * "password": ""
             * }
             */
            // Execute the query
            User user = (User) entityManager.createNativeQuery(query, User.class).getSingleResult();

            // Check if user is found
            if (user == null) {
                throw new HttpException("Invalid username or password", HttpStatus.UNAUTHORIZED);
            }

            return LoginResponseDto.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .accessToken(jwtService.generateAccessToken(user))
                    .ok(true)
                    .build();
        } catch (NoResultException ex) {
            System.out.println(ex.getMessage());
            throw new HttpException("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }
    }
}
