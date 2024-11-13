package com.crewcrew.domain.gathering.dto.response;

import java.util.List;

import com.crewcrew.domain.gathering.entity.Gathering;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GatheringDetailResponse {
  private Long id;
  private Long crewId;
  private String title;
  private String introduce;
  private String dateTime;
  private String location;
  private int currentCount;
  private int totalCount;
  private String imageUrl;
  private List<ParticipantResponse> participants;

  @Builder.Default private boolean isParticipant = false;
  @Builder.Default private boolean isGatheringCaptain = false;
  @Builder.Default private boolean isLiked = false;

  public static GatheringDetailResponse from(
      Gathering gathering, List<ParticipantResponse> participants) {
    return GatheringDetailResponse.builder()
        .id(gathering.getId())
        .crewId(gathering.getCrew().getId())
        .title(gathering.getTitle())
        .introduce(gathering.getIntroduce())
        .dateTime(gathering.getDateTime().toString())
        .location(gathering.getLocation())
        .currentCount(participants.size())
        .totalCount(gathering.getTotalCount())
        .imageUrl(gathering.getImageUrl())
        .participants(participants)
        .build();
  }

  public static GatheringDetailResponse of(
      Gathering gathering,
      List<ParticipantResponse> participants,
      boolean isLiked,
      boolean isGatheringCaptain,
      boolean isParticipant) {
    return GatheringDetailResponse.builder()
        .id(gathering.getId())
        .crewId(gathering.getCrew().getId())
        .title(gathering.getTitle())
        .introduce(gathering.getIntroduce())
        .dateTime(gathering.getDateTime().toString())
        .location(gathering.getLocation())
        .currentCount(participants.size())
        .totalCount(gathering.getTotalCount())
        .imageUrl(gathering.getImageUrl())
        .participants(participants)
        .isParticipant(isParticipant)
        .isGatheringCaptain(isGatheringCaptain)
        .isLiked(isLiked)
        .build();
  }
}
