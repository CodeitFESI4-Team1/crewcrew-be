package com.crewcrew.domain.gathering.controller;

import static com.crewcrew.global.common.exception.SecurityUtil.getCurrentUsername;
import static com.crewcrew.global.common.exception.SecurityUtil.getEmailOrNull;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.crewcrew.domain.gathering.dto.request.GatheringCreateRequest;
import com.crewcrew.domain.gathering.dto.response.GatheringDetailResponse;
import com.crewcrew.domain.gathering.dto.response.GatheringListResponse;
import com.crewcrew.domain.gathering.service.GatheringService;
import com.crewcrew.domain.member.dto.CustomUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/crews/{crewId}/gatherings")
@Tag(name = "약속 기능 API", description = "크루 내 약속 관련 API")
public class GatheringController {

  private final GatheringService gatheringService;

  @Operation(summary = "약속 생성", description = "크루 내 새로운 약속을 생성합니다.")
  @PostMapping
  public ResponseEntity<String> createGathering(
      @Parameter(description = "크루 ID", required = true) @PathVariable Long crewId,
      @Parameter(description = "약속 생성 요청 DTO", required = true) @Valid @RequestBody
          GatheringCreateRequest request,
      @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails) {
    gatheringService.createGathering(crewId, request, getCurrentUsername(userDetails));
    return ResponseEntity.ok("약속이 생성되었습니다.");
  }

  @Operation(
      summary = "약속 상세 조회",
      description = "특정 크루 내 약속의 세부 정보를 조회합니다. " + "참여자 목록, 좋아요 여부, 모임장 여부 등의 정보를 포함합니다.")
  @GetMapping("/{gatheringId}")
  public ResponseEntity<GatheringDetailResponse> getGatheringDetail(
      @Parameter(description = "크루 ID", required = true) @PathVariable Long crewId,
      @Parameter(description = "약속 ID", required = true) @PathVariable Long gatheringId,
      @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails) {
    GatheringDetailResponse response =
        gatheringService.getGatheringDetail(crewId, gatheringId, getEmailOrNull(userDetails));
    return ResponseEntity.ok(response);
  }

  @Operation(
      summary = "약속 참여",
      description = """
                    크루 내 약속에 참여합니다.
                    """)
  @PostMapping("/{gatheringId}/join")
  public ResponseEntity<String> joinGathering(
      @Parameter(description = "크루 ID", required = true) @PathVariable Long crewId,
      @Parameter(description = "약속 ID", required = true) @PathVariable Long gatheringId,
      @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails) {

    gatheringService.joinGathering(crewId, gatheringId, getCurrentUsername(userDetails));
    return ResponseEntity.ok("약속 참여가 완료되었습니다.");
  }

  @Operation(
      summary = "약속 목록 조회",
      description =
          """
                    크루의 모든 약속 목록을 조회합니다.
                    - 조건: 현재 시간 이후의 약속만 조회됩니다.
                    - 정렬: 날짜 기준 내림차순 (최신순)
                    - 기본 정보: 제목, 소개, 일시, 장소, 참여자 수 등
                    """)
  @GetMapping
  public ResponseEntity<List<GatheringListResponse>> getGatheringList(
      @Parameter(description = "크루 ID", required = true) @PathVariable Long crewId,
      @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails) {

    return ResponseEntity.ok(
        gatheringService.getGatheringList(crewId, getEmailOrNull(userDetails)));
  }
}
