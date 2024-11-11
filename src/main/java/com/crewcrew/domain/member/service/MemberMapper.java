package com.crewcrew.domain.member.service;

import com.crewcrew.domain.member.dto.MemberResponse;
import com.crewcrew.domain.member.entity.Member;

public class MemberMapper {

  public static Member toEmailMember(String email, String nickName, String encodedPassword) {
    return Member.builder().email(email).nickName(nickName).password(encodedPassword).build();
  }

  public static MemberResponse.refreshTokenDto toRefreshToken(String refreshToken) {
    return MemberResponse.refreshTokenDto.builder().refreshToken(refreshToken).build();
  }
}
