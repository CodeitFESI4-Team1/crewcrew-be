package com.crewcrew.global.security;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import com.crewcrew.global.exception.GeneralException;
import com.crewcrew.global.payload.ApiPayload;
import com.crewcrew.global.payload.CommonErrorStatus;
import com.crewcrew.global.payload.CommonSuccessStatus;
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

    if (!(request.getMethod().equals("POST")
        && request.getRequestURI().matches("/auths/signout"))) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      writeOutput(
          request,
          response,
          HttpServletResponse.SC_OK,
          ApiPayload.onSuccess(CommonSuccessStatus.OK, null));
    } catch (Exception e) {
      writeOutput(
          request,
          response,
          HttpServletResponse.SC_BAD_REQUEST,
          ApiPayload.onFailure(CommonErrorStatus.BAD_REQUEST, null));
    }
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
