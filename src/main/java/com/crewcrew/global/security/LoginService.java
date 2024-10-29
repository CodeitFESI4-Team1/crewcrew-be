package com.crewcrew.global.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.crewcrew.global.security.jwt.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

  private final JwtUtil jwtUtil;

  @Value("${JWT.ACCESS.EXPIRE}")
  private Long accessExpirationTime;

  @Value("${JWT.REFRESH.EXPIRE}")
  private Long refreshExpirationTime;

  public String issueAccessToken(Long userId) {
    String accessToken = jwtUtil.createJwt("access", userId, accessExpirationTime * 1000L);
    return "Bearer " + accessToken;
  }
}
