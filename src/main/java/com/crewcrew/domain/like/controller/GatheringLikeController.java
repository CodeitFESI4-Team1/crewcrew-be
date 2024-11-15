package com.crewcrew.domain.like.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.crewcrew.domain.like.dto.GatheringLikeResponse;
import com.crewcrew.domain.like.service.GatheringLikeService;
import com.crewcrew.domain.member.dto.CustomUserDetails;
import com.crewcrew.global.common.dto.ApiResponse;
import com.crewcrew.global.common.dto.PaginationResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/liked")
@Tag(name = "찜 기능 API")
public class GatheringLikeController {
  private final GatheringLikeService gatheringLikeService;

  @Operation(summary = "찜 추가하기", description = "모임 찜 추가 API 입니다. 같은 모임에 대해 중복 추가 불가합니다.")
  @PostMapping("{gatheringId}")
  public ResponseEntity<ApiResponse> saveGatheringLike(
      @Parameter(description = "모임 ID", required = true) @PathVariable("gatheringId")
          Long gatheringId,
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    gatheringLikeService.saveGatheringLike(gatheringId, userDetails);

    return ResponseEntity.ok(ApiResponse.of("해당 모임을 찜 하였습니다."));
  }

  @Operation(summary = "찜 해제하기", description = "모임 찜 해제 API 입니다. 해제한 모임에 추가로 해제 불가합니다.")
  @DeleteMapping("{gatheringId}")
  public ResponseEntity<?> DeleteGatheringLike(
      @Parameter(description = "모임 ID", required = true) @PathVariable("gatheringId")
          Long gatheringId,
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    gatheringLikeService.DeleteGatheringLike(gatheringId, userDetails);

    return ResponseEntity.ok(ApiResponse.of("해당 모임에 대한 찜이 해제 되었습니다."));
  }

  @Operation(summary = "내가 찜한 목록 조회", description = "페이지네이션으로 6개씩 불러옵니다.")
  @GetMapping("/memberLikes")
  public ResponseEntity<PaginationResponse<GatheringLikeResponse.GatheringLikeList>> memberLikes(
      @Parameter(description = "페이지 정보 (기본값: 사이즈 6)")
          @PageableDefault(size = 6, sort = "dateTime", direction = Sort.Direction.ASC)
          Pageable pageable,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    PaginationResponse<GatheringLikeResponse.GatheringLikeList> response =
        gatheringLikeService.getMemberLikes(pageable, userDetails);
    return ResponseEntity.ok(response);
  }
}
