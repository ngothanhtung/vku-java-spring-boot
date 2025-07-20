package vku.apiservice.tutorials.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vku.apiservice.tutorials.dtos.AuthResponseDto;
import vku.apiservice.tutorials.dtos.LoginRequestDto;
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

  public AuthResponseDto login(LoginRequestDto loginRequest) {
    // Find user by email (username)
    User user = userService.findByEmail(loginRequest.getUsername())
        .orElseThrow(() -> new HttpException("User not found with email: " + loginRequest.getUsername(), HttpStatus.NOT_FOUND));

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

  private UserDto convertUserToDto(User user) {
    // For now, we'll create a simple conversion. Later we can use the UserService method
    return UserDto.builder()
        .id(user.getId())
        .name(user.getName())
        .email(user.getEmail())
        // .createdAt(user.getCreatedAt())
        // .updatedAt(user.getUpdatedAt())
        // .createdBy(user.getCreatedBy())
        // .updatedBy(user.getUpdatedBy())
        .build();
  }
}
