package com.crewcrew.domain.member.service;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crewcrew.domain.member.dto.MemberRequest;
import com.crewcrew.domain.member.entity.Member;
import com.crewcrew.domain.member.mapper.MemberMapper;
import com.crewcrew.domain.member.repository.MemberRepository;
import com.crewcrew.global.security.LoginService;
import com.crewcrew.global.security.jwt.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
  private final LoginService loginService;
  private final MemberRepository memberRepository;
  private final JwtUtil jwtUtil;
  private final BCryptPasswordEncoder encoder;

  @Transactional
  public void insertMemberByEmail(
      HttpServletResponse response, MemberRequest.joinEmailDto requestDto) {
    Member newMember =
        MemberMapper.toEmailMember(requestDto.getEmail(), encoder.encode(requestDto.getPassword()));
    Member savedMember = memberRepository.save(newMember);

    issueToken(savedMember.getId(), response);
  }

  private void issueToken(Long memberId, HttpServletResponse response) {
    String newAccessToken = loginService.issueAccessToken(memberId);

    response.addHeader("Authorization", newAccessToken);
  }
}
