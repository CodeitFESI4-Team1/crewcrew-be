package com.crewcrew.domain.gathering.dto.response;

import java.util.List;

import com.crewcrew.domain.gathering.entity.Gathering;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GatheringDetailResponse {
  private Long id;
  private String title;
  private String introduce;
  private String dateTime;
  private String location;
  private int currentCount;
  private int totalCount;
  private String imageUrl;
  private boolean isLiked;
  private boolean isGatheringCaptain;
  private List<ParticipantResponse> participants;

  public static GatheringDetailResponse of(
      Gathering gathering,
      boolean isLiked,
      boolean isGatheringCaptain,
      List<ParticipantResponse> participants) {
    return GatheringDetailResponse.builder()
        .id(gathering.getId())
        .title(gathering.getTitle())
        .introduce(gathering.getIntroduce())
        .dateTime(gathering.getDateTime().toString())
        .location(gathering.getLocation())
        .currentCount(participants.size())
        .totalCount(gathering.getTotalCount())
        .imageUrl(gathering.getImageUrl())
        .isLiked(isLiked)
        .isGatheringCaptain(isGatheringCaptain)
        .participants(participants)
        .build();
  }
}
