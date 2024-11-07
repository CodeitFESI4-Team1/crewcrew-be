package com.crewcrew.domain.crew.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.crewcrew.domain.crew.dto.request.CrewCreateRequest;
import com.crewcrew.domain.crew.dto.request.CrewSearchCondition;
import com.crewcrew.domain.crew.dto.request.CrewUpdateRequest;
import com.crewcrew.domain.crew.dto.response.CrewDetailResponse;
import com.crewcrew.domain.crew.dto.response.CrewListResponse;
import com.crewcrew.domain.crew.dto.response.JoinedCrewResponse;
import com.crewcrew.domain.crew.service.CrewService;
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
@RequestMapping("/api/crews")
@Tag(name = "크루 기능 API")
public class CrewController {

  private final CrewService crewService;

  @Operation(summary = "크루 생성", description = "새로운 크루를 생성합니다.")
  @PostMapping
  public ResponseEntity<String> createCrew(
      @Parameter(description = "크루 생성 요청 DTO", required = true) @Validated @RequestBody
          CrewCreateRequest request,
      @AuthenticationPrincipal CustomUserDetails user) {
    crewService.createCrew(request, user.getUsername());
    return ResponseEntity.ok("크루가 생성 되었습니다.");
  }

  @Operation(summary = "크루 상세 조회", description = "특정 크루의 세부 정보를 조회합니다.")
  @GetMapping("/{crewId}")
  public ResponseEntity<CrewDetailResponse> getCrewDetail(
      @Parameter(description = "크루 ID", required = true) @PathVariable Long crewId) {
    CrewDetailResponse response = crewService.getCrewDetail(crewId);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "크루 수정", description = "크루 정보를 수정합니다.")
  @PutMapping("/{crewId}")
  public ResponseEntity<String> updateCrew(
      @Parameter(description = "크루 ID", required = true) @PathVariable Long crewId,
      @Parameter(description = "크루 수정 요청 DTO", required = true) @Validated @RequestBody
          CrewUpdateRequest request,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    crewService.updateCrew(crewId, request, userDetails.getUsername());
    return ResponseEntity.ok("크루가 성공적으로 수정되었습니다.");
  }

  @Operation(summary = "크루 참여", description = "특정 크루에 참여합니다.")
  @PostMapping("/{crewId}/join")
  public ResponseEntity<String> joinCrew(
      @Parameter(description = "크루 ID", required = true) @PathVariable Long crewId,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    crewService.joinCrew(crewId, userDetails.getUsername());
    return ResponseEntity.ok("크루에 참여하였습니다.");
  }

  @Operation(summary = "주최자 크루 취소", description = "주최자가 특정 크루를 취소합니다.")
  @DeleteMapping("/{crewId}")
  public ResponseEntity<Void> deleteCrew(
      @Parameter(description = "크루 ID", required = true) @PathVariable Long crewId,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    crewService.deleteCrew(crewId, userDetails.getUsername());
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "사용자 크루 탈퇴", description = "사용자가 특정 크루에서 탈퇴합니다.")
  @DeleteMapping("/{crewId}/leave")
  public ResponseEntity<String> leaveCrew(
      @Parameter(description = "크루 ID", required = true) @PathVariable Long crewId,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    crewService.leaveCrew(crewId, userDetails.getUsername());
    return ResponseEntity.ok("크루를 탈퇴 했습니다.");
  }

  @Operation(summary = "참여한 크루 조회", description = "사용자가 참여한 크루 목록을 조회합니다. (크루장인 크루 제외)")
  @GetMapping("/joined")
  public ResponseEntity<PagedResponse<JoinedCrewResponse>> getJoinedCrews(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @Parameter(description = "페이지 정보 (기본값: 사이즈 6)")
          @PageableDefault(size = 6, sort = "createdAt", direction = Sort.Direction.DESC)
          Pageable pageable) {
    PagedResponse<JoinedCrewResponse> responses =
        crewService.getJoinedCrews(userDetails.getUsername(), pageable);
    return ResponseEntity.ok(responses);
  }

  @Operation(summary = "주최자의 크루 조회", description = "로그인한 사용자가 크루장인 크루 목록을 조회합니다.")
  @GetMapping("/hosted")
  public ResponseEntity<PagedResponse<JoinedCrewResponse>> getHostedCrews(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @Parameter(description = "페이지 정보 (기본값: 사이즈 6)")
          @PageableDefault(size = 6, sort = "createdAt", direction = Sort.Direction.DESC)
          Pageable pageable) {
    PagedResponse<JoinedCrewResponse> response =
        crewService.getHostedCrews(userDetails.getUsername(), pageable);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "크루 목록 조회", description = "조건에 맞는 크루 목록을 검색합니다.")
  @GetMapping("/search")
  public ResponseEntity<PagedResponse<CrewListResponse>> searchCrews(
      @Parameter(description = "검색 조건, sortType = LATEST, POPULAR") @ModelAttribute
          CrewSearchCondition condition,
      @Parameter(description = "페이지 정보 (기본값: 사이즈 6)")
          @PageableDefault(size = 6, sort = "createdAt", direction = Sort.Direction.DESC)
          Pageable pageable) {
    return ResponseEntity.ok(crewService.searchCrews(condition, pageable));
  }
}
