package vku.apiservice.tutorials.domain.auth.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenRequestDto {
  @NotBlank(message = "Refresh token is required")
  private String refreshToken;
}