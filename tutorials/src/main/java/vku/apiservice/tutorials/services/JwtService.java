package vku.apiservice.tutorials.services;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import vku.apiservice.tutorials.config.JwtConfigProperties;
import vku.apiservice.tutorials.dtos.RoleDto;

/**
 * Service for JWT token operations
 */
@Service
@RequiredArgsConstructor
public class JwtService {

  private final JwtConfigProperties jwtConfig;

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

  public long getJwtExpirationInSeconds() {
    return jwtConfig.getExpiration();
  }
}
