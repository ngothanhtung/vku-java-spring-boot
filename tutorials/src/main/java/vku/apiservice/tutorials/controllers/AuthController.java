package vku.apiservice.tutorials.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vku.apiservice.tutorials.dtos.AuthResponseDto;
import vku.apiservice.tutorials.dtos.LoginDto;
import vku.apiservice.tutorials.services.AuthService;

/**
 * Controller for handling authentication operations
 * Updated with hot reload functionality
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
    return ResponseEntity.status(201).body(response);
  }
}
