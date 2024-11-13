package com.crewcrew.domain.crew.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCrewResponse {
  private Long crewId;

  public static CreateCrewResponse of(Long crewId) {
    return CreateCrewResponse.builder().crewId(crewId).build();
  }
}
