package com.crewcrew.domain.gathering.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GatheringParticipantResponse {
  private Long id;
  private String profileImageUrl;
}
