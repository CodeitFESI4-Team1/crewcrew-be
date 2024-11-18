package com.crewcrew.domain.member.controller;

import static com.crewcrew.global.common.exception.SecurityUtil.getCurrentUsername;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.crewcrew.domain.member.dto.CustomUserDetails;
import com.crewcrew.domain.member.dto.MemberRequest;
import com.crewcrew.domain.member.dto.MemberResponse;
import com.crewcrew.domain.member.service.MemberMapper;
import com.crewcrew.domain.member.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Validated
@Tag(name = "Member", description = "회원 관련 api")
@Slf4j
@RequestMapping("/auths")
public class MemberController {

  private final MemberService memberService;

  @Operation(
      summary = "이메일 회원가입 api",
      description = "헤더의 Authorization에 access 토큰, 바디(쿠키)에 refresh 토큰 반환")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "생성됨"),
        @ApiResponse(
            responseCode = "400",
            description = "유효하지 않은 입력값입니다.\n\n이미 존재하는 이메일입니다.",
            content = {@Content()}),
        @ApiResponse(
            responseCode = "401",
            description = "토큰이 만료되었습니다\n\n토큰이 올바르지 않습니다",
            content = {@Content()}),
        @ApiResponse(
            responseCode = "500",
            description = "서버 에러, 관리자에게 문의하세요\n\n서버 출력에 오류가 있습니다. 관리자에게 문의하세요",
            content = {@Content()})
      })
  @PostMapping("/signup")
  public ResponseEntity<?> joinByEmail(
      HttpServletResponse response, @RequestBody @Valid MemberRequest.joinEmailDto requestDto) {
    String refreshToken = memberService.insertMemberByEmail(response, requestDto);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(MemberMapper.toRefreshToken(refreshToken));
  }

  @Operation(
      summary = "이메일 로그인 api",
      description = "헤더의 Authorization에 access 토큰, 바디(쿠키)에 refresh 토큰 반환")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(
            responseCode = "400",
            description = "유효하지 않은 입력값입니다.",
            content = {@Content()}),
        @ApiResponse(
            responseCode = "500",
            description = "서버 에러, 관리자에게 문의하세요\n\n서버 출력에 오류가 있습니다. 관리자에게 문의하세요",
            content = {@Content()})
      })
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody MemberRequest.loginDto request) {
    // Filter에서 작동하지만, Swagger 위해서 틀만 작성
    return ResponseEntity.ok(null);
  }

  @Operation(summary = "로그아웃 api", description = "Cookie에 refresh 토큰 필요")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(
            responseCode = "400",
            description = "유효하지 않은 입력값입니다.",
            content = {@Content()}),
        @ApiResponse(
            responseCode = "401",
            description = "토큰이 만료되었습니다\n\n토큰이 올바르지 않습니다",
            content = {@Content()}),
        @ApiResponse(
            responseCode = "500",
            description = "서버 에러, 관리자에게 문의하세요\n\n서버 출력에 오류가 있습니다. 관리자에게 문의하세요",
            content = {@Content()})
      })
  @PostMapping("/logout")
  public ResponseEntity<?> logout() {
    // Filter에서 작동하지만, Swagger 위해서 틀만 작성
    return ResponseEntity.ok(null);
  }

  @Operation(summary = "회원 정보 확인 api")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(
            responseCode = "401",
            description = "토큰이 만료되었습니다\n\n토큰이 올바르지 않습니다",
            content = {@Content()}),
        @ApiResponse(
            responseCode = "404",
            description = "회원 정보를 찾을 수 없습니다.",
            content = {@Content()}),
        @ApiResponse(
            responseCode = "500",
            description = "서버 에러, 관리자에게 문의하세요\n\n서버 출력에 오류가 있습니다. 관리자에게 문의하세요",
            content = {@Content()})
      })
  @GetMapping("/user")
  public ResponseEntity<MemberResponse.getMemberInfoDto> getUser(
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    return ResponseEntity.ok(memberService.getMemberInfo(userDetails.getUserId()));
  }

  @Operation(summary = "회원 정보 수정 api", description = "회원 프로필 이미지만 수정 가능합니다.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(
            responseCode = "401",
            description = "토큰이 만료되었습니다\n\n토큰이 올바르지 않습니다",
            content = {@Content()}),
        @ApiResponse(
            responseCode = "404",
            description = "회원 정보를 찾을 수 없습니다.",
            content = {@Content()}),
        @ApiResponse(
            responseCode = "500",
            description = "서버 에러, 관리자에게 문의하세요\n\n서버 출력에 오류가 있습니다. 관리자에게 문의하세요",
            content = {@Content()})
      })
  @PutMapping(value = "/user", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  ResponseEntity<?> updateUser(
      @RequestPart("file") MultipartFile file,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    memberService.updateUser(file, userDetails.getUserId());
    return ResponseEntity.ok(null);
  }

  @Operation(
      summary = "토큰 재발급 api",
      description = "Cookie에 기존 refresh 토큰 필요, 헤더의 Authorization에 access 토큰, 바디(쿠키)에 refresh 토큰 반환")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(
            responseCode = "400",
            description = "잘못된 파라미터 형식입니다",
            content = {@Content()}),
        @ApiResponse(
            responseCode = "401",
            description = "토큰이 만료되었습니다\n\n토큰이 올바르지 않습니다",
            content = {@Content()}),
        @ApiResponse(
            responseCode = "500",
            description = "서버 에러, 관리자에게 문의하세요\n\n서버 출력에 오류가 있습니다. 관리자에게 문의하세요",
            content = {@Content()})
      })
  @PostMapping("/reissue")
  public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
    memberService.reissueToken(request, response);
    return ResponseEntity.ok(com.crewcrew.global.common.dto.ApiResponse.of("토큰이 재발급 되었습니다."));
  }

  @Operation(summary = "유저 프로필 이미지 초기화")
  @PutMapping("/profile-image/reset")
  public ResponseEntity<com.crewcrew.global.common.dto.ApiResponse> resetProfileImage(
      @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails) {
    memberService.resetProfileImage(getCurrentUsername(userDetails));
    return ResponseEntity.ok(com.crewcrew.global.common.dto.ApiResponse.of("기본 이미지로 변경 되었습니다."));
  }
}
