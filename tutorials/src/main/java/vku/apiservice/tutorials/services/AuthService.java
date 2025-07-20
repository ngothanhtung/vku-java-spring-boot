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

    // Verify password (plain text comparison)
    if (!loginRequest.getPassword().equals(user.getPassword())) {
      throw new HttpException("Invalid username or password", HttpStatus.UNAUTHORIZED);
    }

    // Get roles of the user
    List<RoleDto> roles = user.getUserRoles().stream()
        .map(userRole -> new RoleDto(userRole.getRole().getId(), userRole.getRole().getName()))
        .collect(Collectors.toList());

    // Create UserDto for response
    UserDto userDto = new UserDto();
    userDto.setId(user.getId());
    userDto.setName(user.getName());
    userDto.setEmail(user.getEmail());
    userDto.setRoles(roles);

    // Generate access token and refresh token
    String accessToken = jwtService.generateAccessToken(user);
    String refreshToken = jwtService.generateRefreshToken(user);

    // Create and return response
    return new AuthResponseDto(
        userDto,
        accessToken,
        refreshToken,
        jwtExpiration / 1000, // Convert to seconds
        refreshExpiration / 1000 // Convert to seconds
    );
  }

  public AuthResponseDto refreshToken(RefreshTokenDto refreshRequest) {
    try {
      String refreshToken = refreshRequest.getRefreshToken();

      // Extract username from refresh token
      String username = jwtService.extractUsername(refreshToken);

      // Find user
      User user = userService.findByEmail(username)
          .orElseThrow(() -> new HttpException("User not found", HttpStatus.NOT_FOUND));

      // Validate refresh token
      if (!jwtService.isRefreshTokenValid(refreshToken, username)) {
        throw new HttpException("Invalid or expired refresh token", HttpStatus.UNAUTHORIZED);
      }

      // Get user roles
      List<RoleDto> roles = user.getUserRoles().stream()
          .map(userRole -> new RoleDto(userRole.getRole().getId(), userRole.getRole().getName()))
          .collect(Collectors.toList());

      // Create UserDto for response
      UserDto userDto = new UserDto();
      userDto.setId(user.getId());
      userDto.setName(user.getName());
      userDto.setEmail(user.getEmail());
      userDto.setRoles(roles);

      // Generate new access token and refresh token
      String newAccessToken = jwtService.generateAccessToken(user);
      String newRefreshToken = jwtService.generateRefreshToken(user);

      log.info("Token refreshed for user: {}", username);

      return new AuthResponseDto(
          userDto,
          newAccessToken,
          newRefreshToken,
          jwtExpiration / 1000,
          refreshExpiration / 1000);

    } catch (Exception e) {
      log.error("Token refresh failed: {}", e.getMessage());
      throw new HttpException("Invalid refresh token", HttpStatus.UNAUTHORIZED);
    }
  }

  public void logout(String token) {
    try {
      // Extract username from token for logging
      String username = jwtService.extractUsername(token);
      log.info("User logged out: {}", username);

      // In a production environment, you might want to:
      // 1. Add the token to a blacklist (using Redis or similar)
      // 2. Store logout timestamp in database
      // 3. Invalidate refresh tokens associated with this user

    } catch (Exception e) {
      // Handle invalid token during logout gracefully
      log.warn("Logout attempted with invalid token: {}", e.getMessage());
    }
  }
}
