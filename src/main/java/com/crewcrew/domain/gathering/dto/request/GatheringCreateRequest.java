package com.crewcrew.domain.gathering.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import com.crewcrew.domain.crew.entity.Crew;
import com.crewcrew.domain.gathering.entity.Gathering;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GatheringCreateRequest {
  @NotBlank
  @Size(max = 20)
  private String title;

  @Size(max = 100)
  private String introduce;

  @FutureOrPresent private LocalDateTime dateTime;

  @NotBlank private String location;

  @Min(2)
  private Integer totalCount;

  @NotBlank private String imageUrl;

  public Gathering toEntity(Crew crew) {
    return Gathering.builder()
        .crew(crew)
        .title(title)
        .introduce(introduce)
        .dateTime(dateTime)
        .location(location)
        .totalCount(totalCount)
        .imageUrl(imageUrl)
        .build();
  }
}
