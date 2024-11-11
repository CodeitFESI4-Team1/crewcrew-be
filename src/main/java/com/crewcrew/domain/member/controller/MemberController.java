package com.crewcrew.domain.member.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.crewcrew.domain.member.dto.CustomUserDetails;
import com.crewcrew.domain.member.dto.MemberRequest;
import com.crewcrew.domain.member.dto.MemberResponse;
import com.crewcrew.domain.member.service.MemberMapper;
import com.crewcrew.domain.member.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
@Tag(name = "Member", description = "회원 관련 api")
@RequestMapping("/auths")
public class MemberController {

  private final MemberService memberService;

  @Operation(
      summary = "이메일 회원가입 api",
      description = "헤더의 Authorization에 access 토큰, 바디(쿠키)에 refresh 토큰 반환")
  @PostMapping("/signup")
  public ResponseEntity<MemberResponse.refreshTokenDto> joinByEmail(
      HttpServletResponse response, @RequestBody @Valid MemberRequest.joinEmailDto requestDto) {
    String refreshToken = memberService.insertMemberByEmail(response, requestDto);
    return ResponseEntity.ok(MemberMapper.toRefreshToken(refreshToken));
  }

  @Operation(
      summary = "이메일 로그인 api",
      description = "헤더의 Authorization에 access 토큰, 바디(쿠키)에 refresh 토큰 반환")
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody MemberRequest.loginDto request) {
    // Filter에서 작동하지만, Swagger 위해서 틀만 작성
    return ResponseEntity.ok(null);
  }

  @Operation(summary = "로그아웃 api", description = "Cookie에 refresh 토큰 필요")
  @PostMapping("/logout")
  public ResponseEntity<?> logout() {
    // Filter에서 작동하지만, Swagger 위해서 틀만 작성
    return ResponseEntity.ok(null);
  }

  @Operation(summary = "회원 정보 확인 api")
  @GetMapping("/user")
  public ResponseEntity<MemberResponse.getMemberInfoDto> getUser(
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    return ResponseEntity.ok(memberService.getMemberInfo(userDetails.getUserId()));
  }
}
