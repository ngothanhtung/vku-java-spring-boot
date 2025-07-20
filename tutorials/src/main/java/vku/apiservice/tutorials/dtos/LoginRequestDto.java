package vku.apiservice.tutorials.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for user login request
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {
  @NotBlank(message = "Username is required")
  @Email(message = "Username must be a valid email address")
  private String username; // Using email as username

  @NotBlank(message = "Password is required")
  private String password;
}
