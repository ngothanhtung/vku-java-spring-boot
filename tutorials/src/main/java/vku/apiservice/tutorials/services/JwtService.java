package vku.apiservice.tutorials.services;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import vku.apiservice.tutorials.config.JwtConfigProperties;
import vku.apiservice.tutorials.dtos.RoleDto;
import vku.apiservice.tutorials.entities.User;

/**
 * Service for JWT token operations
 */
@Service
@RequiredArgsConstructor
public class JwtService {

  @Value("${application.security.jwt.secret-key}")
  private String secretKey;

  @Value("${application.security.jwt.expiration}")
  private long jwtExpiration;

  @Value("${application.security.jwt.refresh-token.expiration}")
  private long refreshExpiration;

  private final JwtConfigProperties jwtConfig;

  private SecretKey getSigningKey() {
    byte[] keyBytes = io.jsonwebtoken.io.Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  /**
   * Generate ACCESS TOKEN with full user data and roles
   */
  public String generateAccessToken(User user) {
    Map<String, Object> claims = new HashMap<>();

    // Add user roles to access token
    List<Map<String, Object>> roles = user.getUserRoles().stream()
        .map(userRole -> {
          Map<String, Object> roleMap = new HashMap<>();
          roleMap.put("id", userRole.getRole().getId());
          roleMap.put("name", userRole.getRole().getName());
          return roleMap;
        })
        .collect(Collectors.toList());

    claims.put("roles", roles);
    claims.put("userId", user.getId());
    claims.put("type", "access"); // Token type identifier

    return createToken(claims, user.getEmail(), jwtExpiration);
  }

  /**
   * Generate REFRESH TOKEN with minimal data only
   */
  public String generateRefreshToken(User user) {
    Map<String, Object> claims = new HashMap<>();

    // ONLY minimal data for refresh tokens
    claims.put("userId", user.getId()); // Only user ID needed
    claims.put("type", "refresh"); // Token type identifier
    // NO ROLES, NO OTHER SENSITIVE DATA

    return createToken(claims, user.getEmail(), refreshExpiration);
  }

  private String createToken(Map<String, Object> claims, String subject, long expiration) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + expiration);

    return Jwts.builder()
        .claims(claims)
        .subject(subject)
        .issuedAt(now)
        .expiration(expiryDate)
        .signWith(getSigningKey(), Jwts.SIG.HS256)
        .compact();
  }

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public String extractUserId(String token) {
    return extractClaim(token, claims -> claims.get("userId", String.class));
  }

  public String extractTokenType(String token) {
    return extractClaim(token, claims -> claims.get("type", String.class));
  }

  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  private Boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  public Boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    final String tokenType = extractTokenType(token);
    return (username.equals(userDetails.getUsername()))
        && !isTokenExpired(token)
        && "access".equals(tokenType); // Only access tokens for authentication
  }

  /**
   * Validate refresh token - separate method for refresh token validation
   */
  public Boolean isRefreshTokenValid(String refreshToken, String username) {
    try {
      final String tokenUsername = extractUsername(refreshToken);
      final String tokenType = extractTokenType(refreshToken);
      return (tokenUsername.equals(username))
          && !isTokenExpired(refreshToken)
          && "refresh".equals(tokenType); // Must be refresh type
    } catch (Exception e) {
      return false;
    }
  }
}
