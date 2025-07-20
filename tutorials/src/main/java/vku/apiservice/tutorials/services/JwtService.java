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

  private final JwtConfigProperties jwtConfig;

  @Value("${application.security.jwt.secret-key}")
  private String secretKey;

  @Value("${application.security.jwt.expiration}")
  private long jwtExpiration;

  @Value("${application.security.jwt.refresh-token.expiration}")
  private long refreshExpiration;

  private SecretKey getSigningKey() {
    return Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes());
  }

  public String generateToken(String username, String userId, List<RoleDto> roles) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("userId", userId);
    claims.put("roles", roles);
    return createToken(claims, username);
  }

  private String createToken(Map<String, Object> claims, String subject) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + jwtConfig.getExpiration() * 1000);

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

  public Boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  public Boolean validateToken(String token, String username) {
    final String extractedUsername = extractUsername(token);
    return (extractedUsername.equals(username) && !isTokenExpired(token));
  }

  public String generateAccessToken(User user) {
    return generateToken(new HashMap<>(), user, jwtExpiration);
  }

  public String generateRefreshToken(User user) {
    return generateToken(new HashMap<>(), user, refreshExpiration);
  }

  private String generateToken(Map<String, Object> extraClaims, User user, long expiration) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + expiration);

    // Add user roles to claims
    List<Map<String, Object>> roles = user.getUserRoles().stream()
        .map(userRole -> {
          Map<String, Object> roleMap = new HashMap<>();
          roleMap.put("id", userRole.getRole().getId());
          roleMap.put("name", userRole.getRole().getName());
          return roleMap;
        })
        .collect(Collectors.toList());

    extraClaims.put("roles", roles);
    extraClaims.put("userId", user.getId());

    return Jwts.builder()
        .claims(extraClaims)
        .subject(user.getEmail())
        .issuedAt(now)
        .expiration(expiryDate)
        .signWith(getSigningKey(), Jwts.SIG.HS256)
        .compact();
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
  }

  public boolean isRefreshTokenValid(String refreshToken, String username) {
    try {
      final String tokenUsername = extractUsername(refreshToken);
      return (tokenUsername.equals(username)) && !isTokenExpired(refreshToken);
    } catch (Exception e) {
      return false;
    }
  }

  public long getJwtExpirationInSeconds() {
    return jwtConfig.getExpiration();
  }
}
