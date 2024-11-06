package com.crewcrew.domain.crew.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
public class JoinedCrewResponse {
  private final Long id;
  private final String title;
  private final String mainLocation;
  private final String subLocation;
  private final long currentCount;
  private final int totalCount;
  private final String imageUrl;
  private final long totalGathering;
  private final List<CrewMemberResponse> crewMembers;

  @Builder
  public JoinedCrewResponse(
      Long id,
      String title,
      String mainLocation,
      String subLocation,
      long currentCount,
      int totalCount,
      String imageUrl,
      long totalGathering) {
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
      long currentCount,
      int totalCount,
      String imageUrl,
      long totalGathering,
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
    private final String imageUrl;

    @JsonProperty("isCaptain")
    private final boolean isCaptain;

    public CrewMemberResponse(Long id, String nickname, String imageUrl, boolean isCaptain) {
      this.id = id;
      this.nickname = nickname;
      this.imageUrl = imageUrl;
      this.isCaptain = isCaptain;
    }
  }
}
