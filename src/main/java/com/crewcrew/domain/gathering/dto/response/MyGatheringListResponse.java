package com.crewcrew.domain.gathering.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyGatheringListResponse {
  private Long id;
  private Long crewId;
  private String crewTitle;
  private String crewMainLocation;
  private String crewSubLocation;
  private String title;
  private String introduce;
  private LocalDateTime dateTime;
  private String location;
  private String imageUrl;
  private int currentCount;
  private int totalCount;
  private boolean isLiked;

  public MyGatheringListResponse(
      Long id,
      Long crewId,
      String crewTitle,
      String crewMainLocation,
      String crewSubLocation,
      String title,
      String introduce,
      LocalDateTime dateTime,
      String location,
      String imageUrl,
      long currentCount,
      int totalCount,
      boolean isLiked) {
    this.id = id;
    this.crewId = crewId;
    this.crewTitle = crewTitle;
    this.crewMainLocation = crewMainLocation;
    this.crewSubLocation = crewSubLocation;
    this.title = title;
    this.introduce = introduce;
    this.dateTime = dateTime;
    this.location = location;
    this.imageUrl = imageUrl;
    this.currentCount = (int) currentCount;
    this.totalCount = totalCount;
    this.isLiked = isLiked;
  }
}
