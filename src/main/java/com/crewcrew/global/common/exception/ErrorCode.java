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
  USER_NOT_FOUND(UNAUTHORIZED, "인증 정보가 잘못 되었습니다."),

  // Member
  MEMBER_NOT_FOUND(NOT_FOUND, "회원 정보를 찾을 수 없습니다."),
  DUPLICATE_MEMBER_EMAIL(CONFLICT, "이미 존재하는 이메일입니다."),
  INVALID_TOKEN(UNAUTHORIZED, "토큰이 올바르지 않습니다."),
  EXPIRED_TOKEN(UNAUTHORIZED, "토큰이 만료되었습니다."),

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
  INVALID_SUB_CATEGORY(BAD_REQUEST, "유효하지 않은 서브 카테고리입니다."),

  // Gathering
  GATHERING_NOT_FOUND(NOT_FOUND, "모임 정보를 찾을 수 없습니다."),
  INVALID_GATHERING_TITLE(BAD_REQUEST, "모임 제목은 1-20자 이내여야 합니다."),
  INVALID_GATHERING_INTRODUCE(BAD_REQUEST, "모임 소개는 100자 이내여야 합니다."),
  INVALID_GATHERING_DATETIME(BAD_REQUEST, "모임 일시는 현재 시간 이후여야 합니다."),
  INVALID_DATETIME_FORMAT(BAD_REQUEST, "날짜 형식이 올바르지 않습니다."),
  INVALID_GATHERING_TOTAL_COUNT(BAD_REQUEST, "모임 정원은 2명 이상이어야 합니다."),
  GATHERING_CAPACITY_EXCEEDED(CONFLICT, "모임 정원이 초과되었습니다."),
  DUPLICATE_GATHERING_DATETIME(CONFLICT, "해당 시간에 이미 등록된 모임이 있습니다."),
  ALREADY_GATHERING_PARTICIPANT(CONFLICT, "이미 참여 중인 모임입니다."),
  GATHERING_CAPTAIN_LEAVE_DENIED(FORBIDDEN, "모임장은 모임을 탈퇴할 수 없습니다."),
  GATHERING_CAPTAIN_PERMISSION_DENIED(FORBIDDEN, "모임장만 수정/삭제할 수 있습니다."),
  PAST_GATHERING_UPDATE_DENIED(FORBIDDEN, "이미 종료된 모임은 수정할 수 없습니다."),
  GATHERING_CANCELED(BAD_REQUEST, "취소된 모임입니다."),
  GATHERING_COMPLETED(BAD_REQUEST, "종료된 모임입니다."),
  NOT_GATHERING_PARTICIPANT(FORBIDDEN, "모임 참여자가 아닙니다."),
  GATHERING_NOT_IN_CREW(BAD_REQUEST, "해당 크루의 모임이 아닙니다."),
  PAST_GATHERING_JOIN_DENIED(BAD_REQUEST, "지난 약속에는 참여할 수 없습니다."),

  // Image
  IMAGE_REQUIRED(BAD_REQUEST, "이미지 파일은 필수입니다."),
  IMAGE_SIZE_EXCEEDED(BAD_REQUEST, "이미지 크기는 5MB를 초과할 수 없습니다."),
  INVALID_IMAGE_TYPE(BAD_REQUEST, "JPG, JPEG, PNG 형식의 이미지만 업로드 가능합니다."),
  INVALID_IMAGE_FILENAME(BAD_REQUEST, "잘못된 파일명입니다."),

  // Review
  DUPLICATE_REVIEW(CONFLICT, "이미 리뷰를 작성하였습니다.");

  private final HttpStatus httpStatus;
  private final String message;
}
