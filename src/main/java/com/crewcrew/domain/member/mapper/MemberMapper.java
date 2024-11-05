package com.crewcrew.domain.member.mapper;

import com.crewcrew.domain.member.entity.Member;

public class MemberMapper {

  public static Member toEmailMember(String email, String encodedPassword) {
    return Member.builder().email(email).password(encodedPassword).build();
  }
}
