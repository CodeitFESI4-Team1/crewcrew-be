package com.crewcrew.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import com.crewcrew.domain.member.annotation.ExistEmail;

import lombok.Getter;

public class MemberRequest {

  @Getter
  public static class joinEmailDto {
    @NotBlank @Email @ExistEmail String email;

    @NotBlank String nickname;

    @NotBlank String password;
  }

  @Getter
  public static class loginDto {
    String email;
    String password;
  }
}
