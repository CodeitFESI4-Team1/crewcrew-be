package com.crewcrew.domain.gathering.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GatheringListResponse {
  private Long id;
  private String title;
  private String introduce;
  private LocalDateTime dateTime;
  private String location;
  private String imageUrl;
  private int currentCount;
  private int totalCount;
  private boolean isLiked;

  public GatheringListResponse(
      Long id,
      String title,
      String introduce,
      LocalDateTime dateTime,
      String location,
      String imageUrl,
      int totalCount,
      long currentCount,
      boolean isLiked) {
    this.id = id;
    this.title = title;
    this.introduce = introduce;
    this.dateTime = dateTime;
    this.location = location;
    this.imageUrl = imageUrl;
    this.totalCount = totalCount;
    this.currentCount = (int) currentCount;
    this.isLiked = isLiked;
  }
}
