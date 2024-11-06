package com.crewcrew.domain.crew.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CrewDetailResponse {
  private Long id;
  private String title;
  private String mainLocation;
  private String subLocation;
  private int participantCount;
  private int totalCount;
  private String imageUrl;
  private boolean isConfirmed;

  private int totalGatheringCount;
  private List<CrewMemberResponse> crewMembers;

  @Getter
  @Builder
  public static class CrewMemberResponse {
    private Long id;
    private String nickname;
    private String imageUrl;
    private boolean isCaptain;
  }
}
