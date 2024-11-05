package com.crewcrew.global.security;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LogoutFilter extends OncePerRequestFilter {

  private final LoginService loginService;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    if (!(request.getMethod().equals("POST") && request.getRequestURI().matches("/auths/logout"))) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      String refreshToken = loginService.validateRefreshToken(request.getCookies());
      loginService.revokeRefreshToken(refreshToken);

      writeOutput(request, response, HttpServletResponse.SC_OK, ResponseEntity.ok(null));
    } catch (Exception e) {
      writeOutput(
          request,
          response,
          HttpServletResponse.SC_BAD_REQUEST,
          ResponseEntity.badRequest().body(null));
    }
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
