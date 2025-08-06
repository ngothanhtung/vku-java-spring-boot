package com.example.demo.controllers;

import com.example.demo.dtos.GoogleLoginRequestDto;
import com.example.demo.dtos.GoogleLoginWithCredentialRequestDto;
import com.example.demo.dtos.LoginRequestDto;
import com.example.demo.dtos.LoginResponseDto;
import com.example.demo.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto request) throws Exception {
        LoginResponseDto result = this.userService.login(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/google-login")
    public ResponseEntity<LoginResponseDto> googleLogin(@RequestBody @Valid GoogleLoginRequestDto request) throws Exception {
        LoginResponseDto result = this.userService.googleLogin(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/google-login-with-credential")
    public ResponseEntity<LoginResponseDto> googleLoginWithCredential(@RequestBody @Valid GoogleLoginWithCredentialRequestDto request) throws Exception {
        LoginResponseDto result = this.userService.googleLoginWithCredential(request);
        return ResponseEntity.ok(result);
    }
}
