package com.crewcrew.domain.review.controller;

import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.crewcrew.domain.member.dto.CustomUserDetails;
import com.crewcrew.domain.review.dto.ReviewRequest;
import com.crewcrew.domain.review.dto.ReviewResponse;
import com.crewcrew.domain.review.service.ReviewService;
import com.crewcrew.global.common.dto.PagedResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reivew")
@Tag(name = "리뷰 기능 API")
public class ReviewController {
  private final ReviewService reviewService;

  @Operation(summary = "크루 모든 리뷰 조회", description = "해당 크루안에 개설된 모든 모임에 대한 리뷰를 목록으로 확인할 수 있습니다.")
  @GetMapping("{crewId}")
  public ResponseEntity<ReviewResponse.ReviewSummaryAndListResponse> searchReviews(
      @Parameter(description = "크루 ID", required = true) @PathVariable("crewId") Long crewId,
      @Parameter(description = "페이지 정보 (기본값: 사이즈 6)")
          @PageableDefault(size = 6, sort = "createdAt", direction = Sort.Direction.DESC)
          Pageable pageable) {

    // 리뷰 요약 정보 가져오기
    ReviewResponse.ReviewRateInfo rateInfo = reviewService.getReviewRateInfo(crewId);

    // 페이징된 리뷰 리스트 가져오기
    PagedResponse<ReviewResponse.ReviewListInfo> reviewList =
        reviewService.getPagedReviews(crewId, pageable);

    ReviewResponse.ReviewSummaryAndListResponse response =
        ReviewResponse.ReviewSummaryAndListResponse.builder()
            .reviewRateInfo(rateInfo)
            .reviewList(reviewList)
            .build();

    return ResponseEntity.ok(response);
  }

  @Operation(summary = "리뷰 작성", description = "리뷰 작성 API 입니다. 유저 1명당 모임 1개에 최대 1명 작성할 수 있습니다.")
  @PostMapping("{gatheringId}")
  public ResponseEntity<?> makeReview(
      @Parameter(description = "모임 ID", required = true) @PathVariable("gatheringId")
          Long gatheringId,
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @Valid @RequestBody ReviewRequest.ReviewType reviewType) {
    reviewService.makeReview(gatheringId, userDetails, reviewType);
    return ResponseEntity.ok("리뷰가 생성 되었습니다.");
  }

  @Operation(summary = "리뷰 삭제", description = "리뷰 삭제 API 입니다.")
  @DeleteMapping("{reviewId}")
  public ResponseEntity<?> deleteReview(
      @Parameter(description = "리뷰 ID", required = true) @PathVariable("reviewId") Long reviewId,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    reviewService.deleteReview(reviewId, userDetails);
    return ResponseEntity.ok("리뷰가 삭제 되었습니다.");
  }

  @Operation(summary = "내가 작성한 모든 리뷰 목록", description = "무한스크롤로 6개씩 불러옵니다.")
  @GetMapping("/memberReviews")
  public ResponseEntity<PagedResponse<ReviewResponse.MemberReviewListResponse>> memberReviews(
      @PageableDefault(size = 6, sort = "createdAt", direction = Sort.Direction.DESC)
          Pageable pageable,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    PagedResponse<ReviewResponse.MemberReviewListResponse> response =
        reviewService.getMemberReviews(pageable, userDetails);
    return ResponseEntity.ok(response);
  }
}
