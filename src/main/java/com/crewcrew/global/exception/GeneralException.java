package com.crewcrew.global.exception;

import com.crewcrew.global.payload.BaseStatus;
import com.crewcrew.global.payload.ReasonDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

  private BaseStatus code;

  public ReasonDTO getErrorReason() {
    return this.code.getReason();
  }

  public ReasonDTO getErrorReasonHttpStatus() {
    return this.code.getReasonHttpStatus();
  }
}
