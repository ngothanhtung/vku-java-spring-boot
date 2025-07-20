package vku.apiservice.tutorials.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for authentication response containing user information and access token
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDto {
  private UserDto loggedInUser;
  private String accessToken;
  private String refreshToken; // Added refresh token
  private long expiresIn; // Token expiration time in seconds
  private long refreshExpiresIn; // Added refresh token expiration
}
