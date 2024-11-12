package com.crewcrew.domain.review.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReviewRequest {

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class ReviewType {
    @NotNull @Positive private long rate;
    private String comment;
  }
}
