package vku.apiservice.tutorials.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vku.apiservice.tutorials.dtos.AuthResponseDto;
import vku.apiservice.tutorials.dtos.LoginDto;
import vku.apiservice.tutorials.dtos.RoleDto;
import vku.apiservice.tutorials.dtos.UserDto;
import vku.apiservice.tutorials.entities.User;
import vku.apiservice.tutorials.exceptions.HttpException;

/**
 * Service for handling authentication operations
 */
@Service
@RequiredArgsConstructor
public class AuthService {

  private final JwtService jwtService;
  private final UserService userService;

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

    // Generate JWT token
    String accessToken = jwtService.generateToken(user.getEmail(), user.getId(), roles);

    // Convert user to DTO (we need to create this method)
    UserDto userDto = convertUserToDto(user);
    // Set roles in user DTO
    userDto.setRoles(roles);

    return AuthResponseDto.builder()
        .loggedInUser(userDto)
        .accessToken(accessToken)
        .expiresIn(jwtService.getJwtExpirationInSeconds())
        .build();
  }

  public void logout(String token) {
    // For JWT-based authentication, logout can be implemented in different ways:
    // 1. Client-side: Simply remove the token from client storage (simplest)
    // 2. Server-side: Add token to blacklist (more secure but requires storage)

    // For now, we'll implement a simple server-side acknowledgment
    // The actual token invalidation should be handled on the client-side
    // by removing the token from localStorage/sessionStorage

    // Optional: Log the logout event
    if (token != null) {
      try {
        String userEmail = jwtService.extractUsername(token);
        System.out.println("User logged out: " + userEmail);
      } catch (Exception e) {
        // Token might be invalid, but that's ok for logout
        System.out.println("Logout attempt with invalid/expired token");
      }
    }

    // In a production environment, you might want to:
    // 1. Add token to a blacklist/cache (Redis)
    // 2. Log the logout event to audit logs
    // 3. Clear any server-side session data if applicable
  }

  private UserDto convertUserToDto(User user) {
    // For now, we'll create a simple conversion. Later we can use the UserService
    // method
    return UserDto.builder()
        .id(user.getId())
        .name(user.getName())
        .email(user.getEmail())
        .createdAt(user.getCreatedAt())
        .updatedAt(user.getUpdatedAt())
        .createdBy(user.getCreatedBy())
        .updatedBy(user.getUpdatedBy())
        .build();
  }
}
