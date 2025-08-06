package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dtos.LoginRequestDto;
import com.example.demo.dtos.LoginResponseDto;
import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.exceptions.HttpException;
import com.example.demo.repositories.UserJpaRepository;

@DisplayName("UserService Unit Tests")
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final String TEST_USERNAME = "tungnt@softech.vn";
    private static final String TEST_PASSWORD = "123456789";
    private static final String INVALID_PASSWORD = "wrongpassword";
    private static final String EXPECTED_TOKEN = "jwt-token-123456";

    @Mock
    private JwtService jwtService;

    @Mock
    private UserJpaRepository userJpaRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private LoginRequestDto loginRequest;
    private Role testRole;

    @BeforeEach
    void setUp() {
        testRole = new Role();
        testRole.setId(1L);

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername(TEST_USERNAME);
        testUser.setPassword(TEST_PASSWORD);
        testUser.setRoles(List.of(testRole));

        loginRequest = new LoginRequestDto();
        loginRequest.setUsername(TEST_USERNAME);
        loginRequest.setPassword(TEST_PASSWORD);
    }

    @Test
    @DisplayName("Should successfully login with valid credentials")
    void login_ReturnsLoginResponse_WhenCredentialsAreValid() {
        when(userJpaRepository.findByUsername(TEST_USERNAME))
                .thenReturn(Optional.of(testUser));
        when(jwtService.generateAccessToken(testUser))
                .thenReturn(EXPECTED_TOKEN);

        LoginResponseDto response = userService.login(loginRequest);

        assertNotNull(response);
        assertEquals(testUser.getId(), response.getId());
        assertEquals(testUser.getUsername(), response.getUsername());
        assertEquals(EXPECTED_TOKEN, response.getAccessToken());

        verify(userJpaRepository).findByUsername(TEST_USERNAME);
        verify(jwtService).generateAccessToken(testUser);
    }

    @Test
    @DisplayName("Should throw exception when username not found")
    void login_ThrowsException_WhenUsernameNotFound() {
        when(userJpaRepository.findByUsername(TEST_USERNAME))
                .thenReturn(Optional.empty());

        HttpException exception = assertThrows(HttpException.class,
                () -> userService.login(loginRequest));

        assertEquals("Invalid username or password", exception.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());

        verify(userJpaRepository).findByUsername(TEST_USERNAME);
        verifyNoInteractions(jwtService);
    }

    @Test
    @DisplayName("Should throw exception when password is incorrect")
    void login_ThrowsException_WhenPasswordIsIncorrect() {
        loginRequest.setPassword(INVALID_PASSWORD);
        when(userJpaRepository.findByUsername(TEST_USERNAME))
                .thenReturn(Optional.of(testUser));

        HttpException exception = assertThrows(HttpException.class,
                () -> userService.login(loginRequest));

        assertEquals("Invalid username or password", exception.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());

        verify(userJpaRepository).findByUsername(TEST_USERNAME);
        verifyNoInteractions(jwtService);
    }
}