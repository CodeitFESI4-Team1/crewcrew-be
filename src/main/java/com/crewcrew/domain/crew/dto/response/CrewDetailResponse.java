package com.crewcrew.domain.crew.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class CrewDetailResponse {
  private Long id;
  private String title;
  private String introduce;
  private String mainCategory;
  private String subCategory;
  private String mainLocation;
  private String subLocation;
  private Integer participantCount;
  private Integer totalCount;
  private String imageUrl;
  private boolean isConfirmed;

  private Integer totalGatheringCount;
  private List<CrewMemberResponse> crewMembers;

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class CrewMemberResponse {
    private Long id;
    private String nickname;
    private String profileImageUrl;
    private boolean isCaptain;
  }
}
