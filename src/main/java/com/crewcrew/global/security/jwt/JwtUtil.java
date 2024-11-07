package com.crewcrew.global.security.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;

@Component
public class JwtUtil {

  private SecretKey secretKey;

  public JwtUtil(@Value("${jwt.secret.key}") String secret) {
    this.secretKey =
        new SecretKeySpec(
            secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
  }

  public String getCategory(String token) {
    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .get("category", String.class);
  }

  public Long getUserId(String token) {
    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .get("userId", Long.class);
  }

  public String getUserEmail(String token) {
    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .get("userEmail", String.class);
  }

  public Boolean isExpired(String token) {
    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getExpiration()
        .before(new Date());
  }

  public String createJwt(String category, Long userId, String email, Long expiredMs) {
    return Jwts.builder()
        .claim("category", category)
        .claim("userId", userId)
        .claim("userEmail", email)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + expiredMs))
        .signWith(secretKey)
        .compact();
  }
}
