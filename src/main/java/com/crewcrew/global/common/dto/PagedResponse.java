package com.crewcrew.global.common.dto;

import java.util.List;

import lombok.Getter;

@Getter
public class PagedResponse<T> {
  private List<T> content;
  private boolean hasNext;

  public PagedResponse(List<T> content, boolean hasNext) {
    this.content = content;
    this.hasNext = hasNext;
  }
}
