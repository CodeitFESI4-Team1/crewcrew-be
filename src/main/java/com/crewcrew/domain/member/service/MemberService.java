package com.crewcrew.domain.member.service;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.crewcrew.domain.image.entity.ImageType;
import com.crewcrew.domain.image.service.ImageService;
import com.crewcrew.domain.member.dto.MemberRequest;
import com.crewcrew.domain.member.dto.MemberResponse;
import com.crewcrew.domain.member.entity.Member;
import com.crewcrew.domain.member.repository.MemberRepository;
import com.crewcrew.global.common.exception.ApiException;
import com.crewcrew.global.common.exception.ErrorCode;
import com.crewcrew.global.security.LoginService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
  private final LoginService loginService;
  private final MemberRepository memberRepository;
  private final BCryptPasswordEncoder encoder;
  private final ImageService imageService;

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

  @Transactional
  public MemberResponse.getMemberInfoDto getMemberInfo(Long userId) {
    Member member =
        memberRepository
            .findById(userId)
            .orElseThrow(() -> new ApiException(ErrorCode.MEMBER_NOT_FOUND));

    return MemberResponse.getMemberInfoDto
        .builder()
        .id(member.getId())
        .email(member.getEmail())
        .nickname(member.getNickName())
        .profileImageUrl(member.getProfileImageUrl())
        .createdAt(member.getCreatedAt())
        .updatedAt(member.getUpdatedAt())
        .build();
  }

  @Transactional
  public void updateUser(MultipartFile file, Long userId) {
    Member member =
        memberRepository
            .findById(userId)
            .orElseThrow(() -> new ApiException(ErrorCode.MEMBER_NOT_FOUND));

    String profileImageUrl = imageService.uploadImage(ImageType.MEMBER, file);

    member.update(
        member.getEmail(),
        member.getPassword(),
        member.getNickName(),
        profileImageUrl,
        member.getDeletedAt());
  }
}
