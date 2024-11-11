package com.crewcrew.domain.gathering.controller;

import static com.crewcrew.global.common.exception.SecurityUtil.getCurrentUsername;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crewcrew.domain.gathering.dto.response.GatheringReviewResponse;
import com.crewcrew.domain.gathering.service.GatheringService;
import com.crewcrew.domain.member.dto.CustomUserDetails;
import com.crewcrew.global.common.dto.PagedResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/crews/")
@Tag(name = "리뷰 가능한 모임 API", description = "크루 내 약속 리뷰 관련 API")
public class MyGatheringController {
  private final GatheringService gatheringService;

  @Operation(
      summary = "리뷰 가능한 모임 목록 조회",
      description =
          """
                            로그인한 사용자가 참여자로 참여했던 지난 모임 목록을 조회합니다. (주최자로 참여한 모임 제외)
                            - 조건: 현재 시간 이전의 모임만 조회됩니다.
                            - 정렬: 날짜 기준 내림차순 (최신순)
                            - 페이징: 무한 스크롤 방식 (기본 사이즈: 6)
                            """)
  @GetMapping("/reviewable")
  public ResponseEntity<PagedResponse<GatheringReviewResponse>> getReviewableGatherings(
      @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails,
      @Parameter(description = "페이지 정보 (기본값: 사이즈 6)")
          @PageableDefault(size = 6, sort = "dateTime", direction = Sort.Direction.DESC)
          Pageable pageable) {

    return ResponseEntity.ok(
        gatheringService.getReviewableGatherings(getCurrentUsername(userDetails), pageable));
  }
}
