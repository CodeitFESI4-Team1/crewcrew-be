package com.crewcrew.global.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import com.crewcrew.domain.member.dto.CustomUserDetails;
import com.crewcrew.global.exception.GeneralException;
import com.crewcrew.global.payload.ApiPayload;
import com.crewcrew.global.payload.CommonSuccessStatus;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;
  private final LoginService loginService;
  private final ObjectMapper objectMapper = new ObjectMapper();

  public LoginFilter(AuthenticationManager authenticationManager, LoginService loginService) {
    this.authenticationManager = authenticationManager;
    this.loginService = loginService;

    setFilterProcessesUrl("/v1/members/login/email");
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
          ApiPayload.onFailure(LoginErrorStatus.INVALID_PARAMETER, null));
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

    String accessToken = loginService.issueAccessToken(userId);

    response.addHeader("Authorization", accessToken);
    writeOutput(
        request,
        response,
        HttpServletResponse.SC_OK,
        ApiPayload.onSuccess(CommonSuccessStatus.OK, null));
  }

  @Override
  protected void unsuccessfulAuthentication(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
    writeOutput(
        request,
        response,
        HttpServletResponse.SC_UNAUTHORIZED,
        ApiPayload.onFailure(LoginErrorStatus.LOGIN_FAILED, null));
  }

  private void writeOutput(
      HttpServletRequest request,
      HttpServletResponse response,
      int statusCode,
      ApiPayload<?> data) {
    try {
      response.setStatus(statusCode);
      response.setHeader("Content-Type", "application/json");
      response.getOutputStream().write(objectMapper.writeValueAsBytes(data));
    } catch (Exception e) {
      GeneralException newException = new GeneralException(LoginErrorStatus.OUTPUT_ERROR);
      request.setAttribute("exception", newException);
      throw newException;
    }
  }
}
