package vku.apiservice.tutorials.domain.auth.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vku.apiservice.tutorials.domain.auth.dtos.LoginRequestDto;
import vku.apiservice.tutorials.domain.auth.dtos.RefreshTokenRequestDto;
import vku.apiservice.tutorials.domain.security.dtos.RoleResponseDto;
import vku.apiservice.tutorials.domain.security.dtos.UserResponseDto;
import vku.apiservice.tutorials.domain.security.services.UserService;
import vku.apiservice.tutorials.domain.auth.dtos.LoginResponseDto;
import vku.apiservice.tutorials.domain.security.entities.User;
import vku.apiservice.tutorials.presentation.exceptions.HttpException;

/**
 * Service for handling authentication operations
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

  private final JwtService jwtService;
  private final UserService userService;

  @Value("${application.security.jwt.expiration}")
  private long jwtExpiration;

  @Value("${application.security.jwt.refresh-token.expiration}")
  private long refreshExpiration;

  public LoginResponseDto login(LoginRequestDto loginRequest) {
    // Find the user by email (username)
    User user = userService.findByEmail(loginRequest.getUsername())
        .orElseThrow(
            () -> new HttpException("User not found with email: " + loginRequest.getUsername(), HttpStatus.NOT_FOUND));

    // Verify password
    if (!loginRequest.getPassword().equals(user.getPassword())) {
      throw new HttpException("Invalid username or password", HttpStatus.UNAUTHORIZED);
    }

    return generateAuthResponse(user);
  }

  public LoginResponseDto refreshToken(RefreshTokenRequestDto refreshRequest) {
    try {
      String refreshToken = refreshRequest.getRefreshToken();

      // Extract username from a refresh token
      String username = jwtService.extractUsername(refreshToken);

      // Validate refresh token first
      if (!jwtService.isRefreshTokenValid(refreshToken, username)) {
        throw new HttpException("Invalid or expired refresh token", HttpStatus.UNAUTHORIZED);
      }

      // Find the user by email (refresh token only contains minimal data)
      User user = userService.findByEmail(username)
          .orElseThrow(() -> new HttpException("User not found", HttpStatus.NOT_FOUND));

      log.info("Access token refreshed for user: {}", username);

      // Generate new tokens with fresh user data from database
      return generateAuthResponse(user);

    } catch (Exception e) {
      log.error("Token refresh failed: {}", e.getMessage());
      throw new HttpException("Invalid refresh token", HttpStatus.UNAUTHORIZED);
    }
  }

  /**
   * Helper method to generate AuthResponse with fresh user data
   */
  private LoginResponseDto generateAuthResponse(User user) {
    // Get fresh user roles from the database
    List<RoleResponseDto> roles = user.getUserRoles().stream()
        .map(userRole -> new RoleResponseDto(userRole.getRole().getId(), userRole.getRole().getCode(), userRole.getRole().getName()))
        .collect(Collectors.toList());

    // Create UserResponseDto with fresh data
    UserResponseDto userResponseDto = new UserResponseDto();
    userResponseDto.setId(user.getId());
    userResponseDto.setName(user.getName());
    userResponseDto.setEmail(user.getEmail());
    userResponseDto.setRoles(roles);

    // Generate a new access token (with full data + roles)
    String accessToken = jwtService.generateAccessToken(user);

    // Generate a new refresh token (with minimal data only)
    String refreshToken = jwtService.generateRefreshToken(user);

    return new LoginResponseDto(
            userResponseDto,
        accessToken,
        refreshToken,
        jwtExpiration / 1000,
        refreshExpiration / 1000);
  }

  public void logout(String token) {
    try {
      String username = jwtService.extractUsername(token);
      log.info("User logged out: {}", username);

      // Production considerations:
      // 1. Add access token to blacklist
      // 2. Invalidate associated refresh tokens
      // 3. Store logout timestamp

    } catch (Exception e) {
      log.warn("Logout attempted with invalid token: {}", e.getMessage());
    }
  }
}
