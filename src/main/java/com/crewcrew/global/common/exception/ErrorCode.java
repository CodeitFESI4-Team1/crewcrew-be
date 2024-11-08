package com.crewcrew.global.common.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
  // Common
  INVALID_INPUT_VALUE(BAD_REQUEST, "유효하지 않은 입력값입니다."),
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),
  USER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "인증 정보가 잘못 되었습니다."),

  // Member
  MEMBER_NOT_FOUND(NOT_FOUND, "회원 정보를 찾을 수 없습니다."),

  // Crew
  CREW_NOT_FOUND(NOT_FOUND, "크루 정보를 찾을 수 없습니다."),
  CREW_MEMBER_NOT_FOUND(NOT_FOUND, "크루 멤버가 아닙니다."),
  DUPLICATE_CREW_TITLE(CONFLICT, "이미 존재하는 크루 제목입니다."),
  ALREADY_CREW_MEMBER(CONFLICT, "이미 참여 중인 크루입니다."),
  CREW_CAPACITY_EXCEEDED(CONFLICT, "크루 정원이 초과되었습니다."),
  INVALID_TOTAL_COUNT(BAD_REQUEST, "총 인원은 현재 크루원 수 이상이어야 합니다."),
  CAPTAIN_LEAVE_DENIED(FORBIDDEN, "크루장은 크루를 탈퇴할 수 없습니다."),
  CAPTAIN_PERMISSION_DENIED(FORBIDDEN, "크루장만 수정/삭제할 수 있습니다."),
  INVALID_MAIN_CATEGORY(BAD_REQUEST, "유효하지 않은 메인 카테고리입니다."),
  INVALID_SUB_CATEGORY(BAD_REQUEST, "유효하지 않은 서브 카테고리입니다.");

  private final HttpStatus httpStatus;
  private final String message;
}
