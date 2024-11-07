package com.crewcrew.domain.crew.dto.response;

import com.crewcrew.domain.crew.enums.MainCategory;
import com.crewcrew.domain.crew.enums.SubCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrewListResponse {

  private Long id;
  private MainCategory mainCategory;
  private SubCategory subCategory;
  private String title;
  private String mainLocation;
  private String subLocation;
  private Long participantCount;
  private Integer totalCount;
  private String imageUrl;
  private Boolean isConfirmed;
  private Long totalGatheringCount;
}
