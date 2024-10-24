package com.crewcrew.domain.crew.service;

import org.springframework.stereotype.Service;

import com.crewcrew.domain.member.entity.*;
import com.crewcrew.domain.member.repository.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CrewService {

  private final MemberRepository memberRepository;

  private Member findMemberById(Long memberId) {
    return memberRepository
        .findById(memberId)
        .orElseThrow(() -> new RuntimeException("Member not found")); // 임시 에러 처리
  }

  // 임시 처리
  private Long getMemberId() {
    return 1L;
  }
}
