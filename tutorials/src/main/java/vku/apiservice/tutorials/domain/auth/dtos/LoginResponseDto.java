package vku.apiservice.tutorials.domain.auth.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vku.apiservice.tutorials.domain.security.dtos.UserResponseDto;

/**
 * DTO for authentication response containing user information and access token
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
  private UserResponseDto loggedInUser;
  private String accessToken;
  private String refreshToken; // Added refresh token
  private long expiresIn; // Token expiration time in seconds
  private long refreshExpiresIn; // Added refresh token expiration
}
