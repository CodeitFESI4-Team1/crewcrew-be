package com.crewcrew.domain.member.mapper;

import com.crewcrew.domain.member.dto.MemberResponse;
import com.crewcrew.domain.member.entity.Member;

public class MemberMapper {

  public static Member toEmailMember(String email, String encodedPassword) {
    return Member.builder().email(email).password(encodedPassword).build();
  }

  public static MemberResponse.emailCheckDto toEmailCheck(String authCode) {
    return MemberResponse.emailCheckDto.builder().authCode(authCode).build();
  }
}
