package vku.apiservice.tutorials.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import vku.apiservice.tutorials.config.PreAuthorizeUtil;
import vku.apiservice.tutorials.dtos.AuthResponseDto;
import vku.apiservice.tutorials.dtos.LoginDto;
import vku.apiservice.tutorials.dtos.RefreshTokenDto;
import vku.apiservice.tutorials.services.AuthService;

/**
 * Controller for handling authentication operations
 * Updated with refresh token functionality
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  // Endpoint for user login
  @PostMapping("/login")
  public ResponseEntity<AuthResponseDto> login(@RequestBody @Valid LoginDto data) {
    AuthResponseDto response = authService.login(data);
    return ResponseEntity.status(200).body(response);
  }

  // Endpoint for refreshing access token
  @PostMapping("/refresh")
  public ResponseEntity<AuthResponseDto> refreshToken(@RequestBody @Valid RefreshTokenDto data) {
    AuthResponseDto response = authService.refreshToken(data);
    return ResponseEntity.ok(response);
  }

  // Endpoint for user logout
  @PostMapping("/logout")
  @PreAuthorize(PreAuthorizeUtil.ALL_AUTHENTICATED)
  public ResponseEntity<String> logout(HttpServletRequest request) {
    String token = extractTokenFromRequest(request);
    authService.logout(token);
    return ResponseEntity.ok("Logged out successfully");
  }

  private String extractTokenFromRequest(HttpServletRequest request) {
    String authHeader = request.getHeader("Authorization");
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      return authHeader.substring(7);
    }
    return null;
  }
}
