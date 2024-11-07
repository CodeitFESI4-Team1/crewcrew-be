package com.crewcrew.global.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;
import org.springframework.web.server.ResponseStatusException;

import com.crewcrew.domain.member.dto.CustomUserDetails;
import com.crewcrew.domain.member.service.MemberMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;
  private final LoginService loginService;
  private final ObjectMapper objectMapper = new ObjectMapper();

  public LoginFilter(AuthenticationManager authenticationManager, LoginService loginService) {
    this.authenticationManager = authenticationManager;
    this.loginService = loginService;

    setFilterProcessesUrl("/auths/login");
  }

  @Getter
  static class LoginDTO {
    private String email;
    private String password;
  }

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

    LoginDTO loginDTO = new LoginDTO();

    try {
      ServletInputStream inputStream = request.getInputStream();
      String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
      loginDTO = objectMapper.readValue(messageBody, LoginDTO.class);
    } catch (IOException e) {
      writeOutput(
          request,
          response,
          HttpServletResponse.SC_BAD_REQUEST,
          ResponseEntity.badRequest().body(null));
      return null;
    }

    String username = loginDTO.getEmail();
    String password = loginDTO.getPassword();

    UsernamePasswordAuthenticationToken authToken =
        new UsernamePasswordAuthenticationToken(username, password);

    return authenticationManager.authenticate(authToken);
  }

  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authentication) {

    CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

    Long userId = customUserDetails.getUserId();
    String userEmail = customUserDetails.getUsername();

    String accessToken = loginService.issueAccessToken(userId, userEmail);
    //        Cookie refreshToken = loginService.issueRefreshToken(userId);
    String refreshToken = loginService.issueRefreshToken(userId, userEmail);

    response.addHeader("Authorization", accessToken);
    //        response.addCookie(refreshToken);
    writeOutput(
        request,
        response,
        HttpServletResponse.SC_OK,
        ResponseEntity.ok(MemberMapper.toRefreshToken(refreshToken)));
  }

  @Override
  protected void unsuccessfulAuthentication(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
    writeOutput(
        request,
        response,
        HttpServletResponse.SC_UNAUTHORIZED,
        ResponseEntity.badRequest().body(null));
  }

  private void writeOutput(
      HttpServletRequest request,
      HttpServletResponse response,
      int statusCode,
      ResponseEntity<?> data) {
    try {
      response.setStatus(statusCode);
      response.setHeader("Content-Type", "application/json");
      response.getOutputStream().write(objectMapper.writeValueAsBytes(data));
    } catch (Exception e) {
      ResponseStatusException newException = new ResponseStatusException(HttpStatus.BAD_REQUEST);
      request.setAttribute("exception", newException);
      throw newException;
    }
  }
}
