package com.crewcrew.domain.gathering.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GatheringReviewResponse {
  private Long id;
  private String title;
  private LocalDateTime dateTime;
  private String location;
  private int currentCount;
  private int totalCount;
  private String imageUrl;
  private List<GatheringParticipantResponse> participants;

  public GatheringReviewResponse(
      Long id,
      String title,
      LocalDateTime dateTime,
      String location,
      long currentCount,
      int totalCount,
      String imageUrl,
      List<GatheringParticipantResponse> participants) {
    this.id = id;
    this.title = title;
    this.dateTime = dateTime;
    this.location = location;
    this.currentCount = (int) currentCount;
    this.totalCount = totalCount;
    this.imageUrl = imageUrl;
    this.participants = participants;
  }
}
