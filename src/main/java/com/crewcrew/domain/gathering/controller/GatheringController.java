package com.crewcrew.domain.gathering.controller;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.crewcrew.domain.gathering.dto.request.GatheringCreateRequest;
import com.crewcrew.domain.gathering.dto.response.GatheringDetailResponse;
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
    gatheringService.createGathering(crewId, request, userDetails.getUsername());
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
        gatheringService.getGatheringDetail(crewId, gatheringId, userDetails.getUsername());
    return ResponseEntity.ok(response);
  }
}
