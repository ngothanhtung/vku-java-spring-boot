package vku.apiservice.tutorials.domain.auth.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for user login request
 */
@Getter
@Setter
public class LoginRequestDto {
  @NotBlank(message = "Username is required")
  @Email(message = "Username must be a valid email address")
  private String username;

  @NotBlank(message = "Password is required")
  private String password;
}
