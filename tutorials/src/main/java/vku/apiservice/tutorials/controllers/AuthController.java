package vku.apiservice.tutorials.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vku.apiservice.tutorials.dtos.AuthResponseDto;
import vku.apiservice.tutorials.dtos.LoginRequestDto;
import vku.apiservice.tutorials.services.AuthService;

/**
 * Controller for handling authentication operations
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequest) {
    AuthResponseDto response = authService.login(loginRequest);
    return ResponseEntity.ok(response);
  }
}
