package com.solidwall.tartib.core.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

@Component
public class JwtUtil {

  @Value("${security.jwt.secret-key}")
  private String secretKey;

  @Value("${security.jwt.expiration-time}")
  private long jwtExpirationInMs;

  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  private Boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
}

  private Key getSigningKey() {
    return Keys.hmacShaKeyFor(secretKey.getBytes());
  }

  private String createToken(Map<String, Object> claims, String subject) {
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(subject)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  public String generateToken(String username) {
    Map<String, Object> claims = new HashMap<>();
    return createToken(claims, username);
  }

  // New method to generate token with IP address
  public String generateToken(String username, String ipAddress) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("ip", ipAddress);
    claims.put("username", username);

    System.out.println("[DEBUG] JwtUtil - Claims for JWT: " + claims);
    return createToken(claims, username);
  }

  // Method to extract IP address from token
  public String extractIp(String token) {
    return extractClaim(token, claims -> (String) claims.get("ip"));
  }
  

  public Boolean validateToken(String token, String username) {
    final String extractedUsername = extractUsername(token);
    return (extractedUsername.equals(username) && !isTokenExpired(token));
  }
   public Boolean validateToken(String token) {
    
    return (!isTokenExpired(token));
  }
  public Boolean isTokenExpiringSoon(String token) {
      Date expiration = extractExpiration(token);
      Date now = new Date();
      long timeUntilExpiration = expiration.getTime() - now.getTime();
      // Consider "soon" as 5 minutes (300000ms)
      return timeUntilExpiration <= 300000 && timeUntilExpiration > 0;
  }

  public String refreshToken(String token) {
      String username = extractUsername(token);
      String ipAddress = extractIp(token);
      return generateToken(username, ipAddress);
  }
}
