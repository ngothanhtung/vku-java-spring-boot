package vku.apiservice.tutorials.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "app.jwt")
public class JwtConfigProperties {

  /**
   * Secret key used for JWT token signing and validation
   */
  private String secret = "myVerySecretJwtKey1234567890123456789012345678901234567890";

  /**
   * JWT token expiration time in seconds (default: 24 hours)
   */
  private Long expiration = 86400L;
}
