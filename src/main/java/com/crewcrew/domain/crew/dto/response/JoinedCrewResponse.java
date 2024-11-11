package com.crewcrew.domain.crew.dto.response;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
public class JoinedCrewResponse {
  private Long id;
  private String title;
  private String mainLocation;
  private String subLocation;
  private Long currentCount;
  private Long totalCount;
  private String imageUrl;
  private Long totalGathering;
  private final List<CrewMemberResponse> crewMembers;

  @Builder
  public JoinedCrewResponse(
      Long id,
      String title,
      String mainLocation,
      String subLocation,
      Long currentCount,
      Long totalCount,
      String imageUrl,
      Long totalGathering) {
    this(
        id,
        title,
        mainLocation,
        subLocation,
        currentCount,
        totalCount,
        imageUrl,
        totalGathering,
        new ArrayList<>());
  }

  @Builder
  public JoinedCrewResponse(
      Long id,
      String title,
      String mainLocation,
      String subLocation,
      Long currentCount,
      Long totalCount,
      String imageUrl,
      Long totalGathering,
      List<CrewMemberResponse> crewMembers) {
    this.id = id;
    this.title = title;
    this.mainLocation = mainLocation;
    this.subLocation = subLocation;
    this.currentCount = currentCount;
    this.totalCount = totalCount;
    this.imageUrl = imageUrl;
    this.totalGathering = totalGathering;
    this.crewMembers = crewMembers;
  }

  public JoinedCrewResponse withCrewMembers(List<CrewMemberResponse> crewMembers) {
    return new JoinedCrewResponse(
        this.id,
        this.title,
        this.mainLocation,
        this.subLocation,
        this.currentCount,
        this.totalCount,
        this.imageUrl,
        this.totalGathering,
        crewMembers);
  }

  @Getter
  public static class CrewMemberResponse {
    private final Long id;
    private final String nickname;
    private final String profileImageUrl;
    private final boolean isCaptain;

    public CrewMemberResponse(Long id, String nickname, String profileImageUrl, boolean isCaptain) {
      this.id = id;
      this.nickname = nickname;
      this.profileImageUrl = profileImageUrl;
      this.isCaptain = isCaptain;
    }
  }
}
