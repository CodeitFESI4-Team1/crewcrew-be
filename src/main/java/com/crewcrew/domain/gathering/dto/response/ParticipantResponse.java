package com.crewcrew.domain.gathering.dto.response;

import com.crewcrew.domain.member.entity.Member;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ParticipantResponse {
  private Long id;
  private String profileImageUrl;
  private String nickname;
  private String email;

  public static ParticipantResponse from(Member member) {
    return ParticipantResponse.builder()
        .id(member.getId())
        .profileImageUrl(member.getProfileImageUrl())
        .nickname(member.getNickName())
        .email(member.getEmail())
        .build();
  }
}
