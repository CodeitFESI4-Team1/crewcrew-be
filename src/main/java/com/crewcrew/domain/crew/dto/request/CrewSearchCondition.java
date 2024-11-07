package com.crewcrew.domain.crew.dto.request;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CrewSearchCondition {
  private String keyword;
  private String mainLocation;
  private String mainCategory;
  private String subCategory;
  private SortType sortType = SortType.LATEST;

  @Getter
  @RequiredArgsConstructor
  public enum SortType {
    LATEST("최신순"),
    POPULAR("인기순");
    private final String description;
  }
}
