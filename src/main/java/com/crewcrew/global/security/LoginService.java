package com.crewcrew.global.security;

import java.time.LocalDateTime;
import java.time.ZoneId;

import jakarta.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.crewcrew.domain.member.entity.Refresh;
import com.crewcrew.domain.member.repository.RefreshRepository;
import com.crewcrew.global.security.jwt.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

  private final JwtUtil jwtUtil;
  private final RefreshRepository refreshRepository;

  @Value("${JWT.ACCESS.EXPIRE}")
  private Long accessExpirationTime;

  @Value("${JWT.REFRESH.EXPIRE}")
  private Long refreshExpirationTime;

  public String issueAccessToken(Long userId, String userEmail) {
    String accessToken =
        jwtUtil.createJwt("access", userId, userEmail, accessExpirationTime * 1000L);
    return "Bearer " + accessToken;
  }

  @Transactional
  public String issueRefreshToken(Long userId, String userEmail) {
    String refreshToken =
        jwtUtil.createJwt("refresh", userId, userEmail, refreshExpirationTime * 1000L);
    saveRefreshToken(userId, refreshToken, refreshExpirationTime);
    return refreshToken;
  }

  @Transactional
  public String reissueRefreshToken(Long userId, String userEmail, String refreshToken) {
    refreshRepository.deleteByRefreshToken(refreshToken);
    String newRefreshToken =
        jwtUtil.createJwt("refresh", userId, userEmail, refreshExpirationTime * 1000L);
    saveRefreshToken(userId, newRefreshToken, refreshExpirationTime);
    return newRefreshToken;
  }

  @Transactional
  public void revokeRefreshToken(String refreshToken) {
    refreshRepository.deleteByRefreshToken(refreshToken);
  }

  public String validateRefreshToken(Cookie[] cookies) {
    String refreshToken = null;
    for (Cookie cookie : cookies) {
      if ("refresh".equals(cookie.getName())) {
        refreshToken = cookie.getValue();
        break;
      }
    }

    if (refreshToken == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token not found");
    }

    try {
      jwtUtil.isExpired(refreshToken);
    } catch (ExpiredJwtException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token expired");
    }

    String category = jwtUtil.getCategory(refreshToken);
    if (!"refresh".equals(category)) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token category");
    }

    boolean isExist = refreshRepository.existsByRefreshToken(refreshToken);
    if (!isExist) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
    }

    return refreshToken;
  }

  private void saveRefreshToken(Long userId, String refreshToken, Long expirationTime) {
    LocalDateTime date = LocalDateTime.now(ZoneId.systemDefault()).plusSeconds(expirationTime);
    Refresh newRefresh =
        Refresh.builder().userId(userId).refreshToken(refreshToken).expiredAt(date).build();

    refreshRepository.save(newRefresh);
  }
}
