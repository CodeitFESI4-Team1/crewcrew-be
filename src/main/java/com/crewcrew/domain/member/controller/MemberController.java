package com.crewcrew.domain.member.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crewcrew.domain.member.dto.MemberRequest;
import com.crewcrew.domain.member.service.MemberService;
import com.crewcrew.global.payload.ApiPayload;
import com.crewcrew.global.payload.CommonSuccessStatus;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
@Tag(name = "Member", description = "회원 관련 api")
@RequestMapping("/auths")
public class MemberController {
  private final MemberService memberService;

  @Operation(summary = "이메일 회원가입 api", description = "헤더의 Authorization에 access 토큰")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "생성됨"),
        @ApiResponse(
            responseCode = "400",
            description = "COMMON_400 : 잘못된 요청",
            content = {@Content()}),
        @ApiResponse(
            responseCode = "500",
            description = "COMMON_500 : 서버 에러, 관리자에게 문의하세요",
            content = {@Content()})
      })
  @PostMapping("/join/email")
  public ApiPayload<?> joinByEmail(
      HttpServletResponse response, @RequestBody @Valid MemberRequest.joinEmailDto requestDto) {
    memberService.insertMemberByEmail(response, requestDto);
    return ApiPayload.onSuccess(CommonSuccessStatus.CREATED, null);
  }

  @Operation(summary = "이메일 로그인 api", description = "헤더의 Authorization에 access 토큰")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "생성됨"),
        @ApiResponse(
            responseCode = "400",
            description = "AUTH_4000 : 잘못된 파라미터 형식입니다",
            content = {@Content()}),
        @ApiResponse(
            responseCode = "401",
            description =
                "AUTH_4010 : 로그인 정보가 잘못되었습니다\n\nAUTH_4011 : 토큰이 존재하지 않습니다\n\nAUTH_4012 : 토큰이 만료되었습니다\n\nAUTH_4013 : 토큰이 올바르지 않습니다",
            content = {@Content()}),
        @ApiResponse(
            responseCode = "500",
            description =
                "COMMON_500 : 서버 에러, 관리자에게 문의하세요\n\nAUTH_5000 : 서버 출력에 오류가 있습니다. 관리자에게 문의하세요",
            content = {@Content()})
      })
  @PostMapping("/login/email")
  public ApiPayload<?> login(@RequestBody MemberRequest.loginDto request) {
    // Filter에서 작동하지만, Swagger 위해서 틀만 작성
    return ApiPayload.onSuccess(CommonSuccessStatus.OK, null);
  }

  @Operation(summary = "로그아웃 api", description = "Cookie에 refresh 토큰 필요")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(
            responseCode = "400",
            description = "AUTH_4000 : 잘못된 파라미터 형식입니다",
            content = {@Content()}),
        @ApiResponse(
            responseCode = "401",
            description =
                "AUTH_4010 : 로그인 정보가 잘못되었습니다\n\nAUTH_4011 : 토큰이 존재하지 않습니다\n\nAUTH_4012 : 토큰이 만료되었습니다\n\nAUTH_4013 : 토큰이 올바르지 않습니다",
            content = {@Content()}),
        @ApiResponse(
            responseCode = "500",
            description =
                "COMMON_500 : 서버 에러, 관리자에게 문의하세요\n\nAUTH_5000 : 서버 출력에 오류가 있습니다. 관리자에게 문의하세요",
            content = {@Content()})
      })
  @PostMapping("/logout")
  public ApiPayload<?> logout() {
    // Filter에서 작동하지만, Swagger 위해서 틀만 작성
    return ApiPayload.onSuccess(CommonSuccessStatus.OK, null);
  }
}
