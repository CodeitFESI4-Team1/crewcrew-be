package com.crewcrew.domain.review.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.crewcrew.global.common.dto.PaginationResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class ReviewResponse {
  @Getter
  @Builder
  public static class ReviewRateInfo {
    private long totalRate;
    private double averageRate;
    List<RatingsData> ratingsData;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  public static class RatingsData {
    private long score;
    private long count;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  public static class ReviewListInfo {
    private Long crewId;
    private Long id;
    private long rate;
    private String comment;
    private LocalDateTime createdAt;
    private ReviewerType reviewer;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  public static class ReviewerType {
    private Long id;
    private String nickname;
    private String profileImageUrl;
  }

  @Getter
  @Builder
  public static class ReviewSummaryAndListResponse {
    private ReviewRateInfo reviewRateInfo;
    private PaginationResponse<ReviewListInfo> reviewList;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  public static class MemberReviewListResponse {
    private Long crewId;
    private String crewName;
    private String gatheringName;
    private Long id;
    private long rate;
    private String comment;
    private LocalDateTime createdAt;
    private String gatheringLocation;
  }
}
