package vku.apiservice.tutorials.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vku.apiservice.tutorials.dtos.AuthResponseDto;
import vku.apiservice.tutorials.dtos.LoginDto;
import vku.apiservice.tutorials.dtos.RefreshTokenDto;
import vku.apiservice.tutorials.dtos.RoleDto;
import vku.apiservice.tutorials.dtos.UserDto;
import vku.apiservice.tutorials.entities.User;
import vku.apiservice.tutorials.exceptions.HttpException;

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

  public AuthResponseDto login(LoginDto loginRequest) {
    // Find user by email (username)
    User user = userService.findByEmail(loginRequest.getUsername())
        .orElseThrow(
            () -> new HttpException("User not found with email: " + loginRequest.getUsername(), HttpStatus.NOT_FOUND));

    // Verify password
    if (!loginRequest.getPassword().equals(user.getPassword())) {
      throw new HttpException("Invalid username or password", HttpStatus.UNAUTHORIZED);
    }

    return generateAuthResponse(user);
  }

  public AuthResponseDto refreshToken(RefreshTokenDto refreshRequest) {
    try {
      String refreshToken = refreshRequest.getRefreshToken();

      // Extract username from refresh token
      String username = jwtService.extractUsername(refreshToken);

      // Validate refresh token first
      if (!jwtService.isRefreshTokenValid(refreshToken, username)) {
        throw new HttpException("Invalid or expired refresh token", HttpStatus.UNAUTHORIZED);
      }

      // Find user by email (refresh token only contains minimal data)
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
  private AuthResponseDto generateAuthResponse(User user) {
    // Get fresh user roles from database
    List<RoleDto> roles = user.getUserRoles().stream()
        .map(userRole -> new RoleDto(userRole.getRole().getId(), userRole.getRole().getName()))
        .collect(Collectors.toList());

    // Create UserDto with fresh data
    UserDto userDto = new UserDto();
    userDto.setId(user.getId());
    userDto.setName(user.getName());
    userDto.setEmail(user.getEmail());
    userDto.setRoles(roles);

    // Generate new access token (with full data + roles)
    String accessToken = jwtService.generateAccessToken(user);

    // Generate new refresh token (with minimal data only)
    String refreshToken = jwtService.generateRefreshToken(user);

    return new AuthResponseDto(
        userDto,
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
