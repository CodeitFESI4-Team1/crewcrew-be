package com.crewcrew.domain.like.dto;

import java.time.LocalDateTime;

import lombok.*;

public class GatheringLikeResponse {

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class GatheringLikeList {
    private Long id;
    private String title;
    private LocalDateTime dateTime;
    private String location;
    private long currentCount;
    private Integer totalCount;
    private String imageUrl;
  }
}
