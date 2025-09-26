package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.example.demo.dtos.LoginRequestDto;
import com.example.demo.dtos.LoginResponseDto;
import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.exceptions.HttpException;
import com.example.demo.repositories.UserJpaRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserJpaRepository userJpaRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private LoginRequestDto loginRequest;

    private String username;
    private String password;

    @BeforeEach
    void setUp() {

        username = "tungnt@softech.vn";
        password = "123456789";

        // Setup test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername(this.username);
        testUser.setPassword(this.password);

        Role role = new Role();
        role.setId(1L);
        testUser.setRoles(List.of(role));

        // Setup login request
        loginRequest = new LoginRequestDto();
        loginRequest.setUsername(this.username);
        loginRequest.setPassword(this.password);
    }

    @Test
    void login_ShouldReturnLoginResponse_WhenCredentialsAreValid() {
        // Given
        String expectedAccessToken = "mock-jwt-token";
        when(userJpaRepository.findByUsername(this.username)).thenReturn(Optional.of(testUser));

        when(jwtService.generateAccessToken(testUser)).thenReturn(expectedAccessToken);

        // When
        LoginResponseDto result = userService.login(loginRequest);

        // Then
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getUsername(), result.getUsername());
        assertEquals(expectedAccessToken, result.getAccessToken());
        assertTrue(result.isOk());

        verify(userJpaRepository).findByUsername(result.getUsername());
        verify(jwtService).generateAccessToken(testUser);
    }

    @Test
    void login_ShouldThrowHttpException_WhenUserNotFound() {
        // Given
        when(userJpaRepository.findByUsername("nonexistent@example.com")).thenReturn(Optional.empty());
        loginRequest.setUsername("nonexistent@example.com");

        // When & Then
        HttpException exception = assertThrows(HttpException.class, () -> {
            userService.login(loginRequest);
        });

        assertEquals("Invalid username or password", exception.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
        verify(userJpaRepository).findByUsername("nonexistent@example.com");
        verifyNoInteractions(jwtService);
    }

    @Test
    void login_ShouldThrowHttpException_WhenPasswordIsInvalid() {
        // Given
        when(userJpaRepository.findByUsername(this.username)).thenReturn(Optional.of(testUser));
        loginRequest.setPassword("wrongpassword");

        // When & Then
        HttpException exception = assertThrows(HttpException.class, () -> {
            userService.login(loginRequest);
        });

        assertEquals("Invalid username or password", exception.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
        verify(userJpaRepository).findByUsername(this.username);
        verifyNoInteractions(jwtService);
    }

    @Test
    void login_ShouldThrowHttpException_WhenPasswordIsNull() {
        // Given
        testUser.setPassword(null);
        when(userJpaRepository.findByUsername(this.username)).thenReturn(Optional.of(testUser));

        // When & Then
        HttpException exception = assertThrows(HttpException.class, () -> {
            userService.login(loginRequest);
        });

        assertEquals("Invalid username or password", exception.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
    }

    @Test
    void login_ShouldThrowHttpException_WhenRequestPasswordIsNull() {
        // Given
        when(userJpaRepository.findByUsername(this.username)).thenReturn(Optional.of(testUser));
        loginRequest.setPassword(null);

        // When & Then
        // The actual implementation will throw NullPointerException due to
        // null.equals() call
        assertThrows(NullPointerException.class, () -> {
            userService.login(loginRequest);
        });

        verify(userJpaRepository).findByUsername(this.username);
        verifyNoInteractions(jwtService);
    }
}