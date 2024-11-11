package com.crewcrew.domain.member.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

public class MemberResponse {

  @Getter
  @Builder
  public static class refreshTokenDto {
    String refreshToken;
  }

  @Getter
  @Builder
  public static class getMemberInfoDto {
    Long id;
    String email;
    String nickname;
    String profileImageUrl;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
  }
}
