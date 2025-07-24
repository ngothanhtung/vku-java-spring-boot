package vku.apiservice.tutorials.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "application.security.jwt")
public class JwtConfigProperties {

  /**
   * Secret key used for JWT token signing and validation
   */
  private String secret = "MIsMiHz45ATNS6elM6dQLfN6oQIBDSV+KbAc5PE3rlA=";

  /**
   * JWT token expiration time in seconds (default: 24 hours)
   */
  private Long expiration = 86400L;
}
