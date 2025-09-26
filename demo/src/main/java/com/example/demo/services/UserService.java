package com.example.demo.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dtos.GoogleLoginRequestDto;
import com.example.demo.dtos.GoogleLoginWithCredentialRequestDto;
import com.example.demo.dtos.LoginRequestDto;
import com.example.demo.dtos.LoginResponseDto;
import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.exceptions.HttpException;
import com.example.demo.repositories.UserJpaRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Validated
@Service
public class UserService {
    private final JwtService jwtService;
    private final UserJpaRepository userJpaRepository;
    private final RestTemplate restTemplate;

    public LoginResponseDto login(@Valid LoginRequestDto request) {

        // Find the user by email (username)
        User user = this.userJpaRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new HttpException("Invalid username or password", HttpStatus.UNAUTHORIZED));

        // Verify password
        if (!request.getPassword().equals(user.getPassword())) {
            throw new HttpException("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }

        // Generate a new access token (with full data + roles)
        String accessToken = jwtService.generateAccessToken(user);

        return LoginResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .accessToken(accessToken)
                .ok(true)
                .build();
    }

    public LoginResponseDto googleLogin(GoogleLoginRequestDto request) {
        // Find the user by email
        Optional<User> user = this.userJpaRepository.findByEmail(request.getEmail());
        // Create new user if not found
        User newUser = new User();
        String accessToken;
        if (user.isEmpty()) {

            newUser.setUsername(request.getEmail());
            newUser.setPassword(""); // Password not used for Google login

            Role role = new Role();
            role.setId(3L);
            newUser.setRoles(List.of(role)); // Assuming role ID 3 for Google users
            // Save the new user
            this.userJpaRepository.save(newUser);
        } else {
            newUser = user.get();
        }

        // Generate a new access token (with full data + roles)
        accessToken = jwtService.generateAccessToken(newUser);

        return LoginResponseDto.builder()
                .id(newUser.getId())
                .username(newUser.getUsername())
                .accessToken(accessToken)
                .build();
    }

    public LoginResponseDto googleLoginWithCredential(GoogleLoginWithCredentialRequestDto request) {
        // Call google API to verify the token
        String url = "https://oauth2.googleapis.com/tokeninfo?id_token=" + request.getCredential();
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        Map<String, Object> payload = response.getBody();

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new HttpException("Invalid Google token", HttpStatus.UNAUTHORIZED);
        }

        // Parse the response to get the email
        String email;
        if (payload != null && payload.containsKey("email")) {
            email = payload.get("email").toString();
        } else {
            throw new HttpException("Email not found in token", HttpStatus.UNAUTHORIZED);
        }

        // Nên kiểm tra aud = Client ID của ứng dụng để đảm bảo token hợp lệ ( // Có thể
        // code sau ...)
        // String aud = payload.get("aud").toString();
        // if (!aud.equals("YOUR_CLIENT_ID")) {
        // throw new HttpException("Invalid Google token audience",
        // HttpStatus.UNAUTHORIZED);
        // }

        // Kiểm tra thêm: exp so với thời gian hiện tại để đảm bảo token chưa hết hạn.
        String iss = payload.get("iss").toString();
        if (!iss.equals("https://accounts.google.com") && !iss.equals("accounts.google.com")) {
            throw new HttpException("Invalid Google token issuer", HttpStatus.UNAUTHORIZED);
        }

        // Kiểm tra thêm: exp so với thời gian hiện tại để đảm bảo token chưa hết hạn.
        long exp = Long.parseLong(payload.get("exp").toString());
        if (exp < System.currentTimeMillis() / 1000) {
            throw new HttpException("Google token has expired", HttpStatus.UNAUTHORIZED);
        }

        // Find the user by email
        // If not found, create a new user with the email.
        Optional<User> user = this.userJpaRepository.findByEmail(email);

        // Create new user if not found
        User newUser = user.orElseGet(() -> {
            User u = new User();
            u.setUsername(email);
            u.setPassword("");
            Role role = new Role();
            role.setId(3L);
            u.setRoles(List.of(role));
            return userJpaRepository.save(u);
        });

        // Generate a new access token (with full data + roles)
        String accessToken = jwtService.generateAccessToken(newUser);

        return LoginResponseDto.builder()
                .id(newUser.getId())
                .username(newUser.getUsername())
                .accessToken(accessToken)
                .build();
    }
}
