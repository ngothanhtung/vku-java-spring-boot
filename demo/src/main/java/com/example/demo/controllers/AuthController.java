package com.example.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.GoogleLoginRequestDto;
import com.example.demo.dtos.GoogleLoginWithCredentialRequestDto;
import com.example.demo.dtos.LoginRequestDto;
import com.example.demo.dtos.LoginResponseDto;
import com.example.demo.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication management APIs including login and Google OAuth")
public class AuthController {

    private final UserService userService;

    @Operation(summary = "User login", description = "Authenticate user with email and password, returns JWT token for subsequent API calls")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto request) throws Exception {
        LoginResponseDto result = this.userService.login(request);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Google OAuth login", description = "Authenticate user with Google OAuth token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Google login successful", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Invalid Google token", content = @Content)
    })
    @PostMapping("/google-login")
    public ResponseEntity<LoginResponseDto> googleLogin(@RequestBody @Valid GoogleLoginRequestDto request)
            throws Exception {
        LoginResponseDto result = this.userService.googleLogin(request);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Google OAuth login with credentials", description = "Authenticate user with Google OAuth credentials")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Google credential login successful", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Invalid Google credentials", content = @Content)
    })
    @PostMapping("/google-login-with-credential")
    public ResponseEntity<LoginResponseDto> googleLoginWithCredential(
            @RequestBody @Valid GoogleLoginWithCredentialRequestDto request) throws Exception {
        LoginResponseDto result = this.userService.googleLoginWithCredential(request);
        return ResponseEntity.ok(result);
    }
}
