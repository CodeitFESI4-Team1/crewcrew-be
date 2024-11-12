package com.crewcrew.domain.review.dto;

import jakarta.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;

public class ReviewRequest {

  @Getter
  @Builder
  public static class ReviewType {
    @NotNull private long rate;
    private String comment;
  }
}
