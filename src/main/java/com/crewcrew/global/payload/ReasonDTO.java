package com.crewcrew.global.payload;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReasonDTO {

  private HttpStatus httpStatus;

  private final Boolean isSuccess;
  private final String code;
  private final String message;
}
