package com.crewcrew.global.common.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ErrorResponse {

  private HttpStatus status;
  private String message;
  private Map<String, String> validationErrors;

  public ErrorResponse(ErrorCode errorCode) {
    this.status = errorCode.getHttpStatus();
    this.message = errorCode.getMessage();
  }

  public ErrorResponse(ErrorCode errorCode, Map<String, String> validationErrors) {
    this.status = errorCode.getHttpStatus();
    this.message = errorCode.getMessage();
    this.validationErrors = validationErrors;
  }
}
