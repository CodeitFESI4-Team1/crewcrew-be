package com.crewcrew.domain.member.service;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crewcrew.domain.member.dto.MemberRequest;
import com.crewcrew.domain.member.entity.Member;
import com.crewcrew.domain.member.repository.MemberRepository;
import com.crewcrew.global.security.LoginService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
  private final LoginService loginService;
  private final MemberRepository memberRepository;
  private final BCryptPasswordEncoder encoder;

  @Transactional
  public String insertMemberByEmail(
      HttpServletResponse response, MemberRequest.joinEmailDto requestDto) {
    Member newMember =
        MemberMapper.toEmailMember(
            requestDto.getEmail(),
            requestDto.getNickName(),
            encoder.encode(requestDto.getPassword()));
    Member savedMember = memberRepository.save(newMember);

    return issueToken(savedMember.getId(), savedMember.getEmail(), response);
  }

  private String issueToken(Long memberId, String userEmail, HttpServletResponse response) {
    String newAccessToken = loginService.issueAccessToken(memberId, userEmail);
    String newRefreshToken = loginService.issueRefreshToken(memberId, userEmail);

    response.addHeader("Authorization", newAccessToken);
    return newRefreshToken;
  }
}
