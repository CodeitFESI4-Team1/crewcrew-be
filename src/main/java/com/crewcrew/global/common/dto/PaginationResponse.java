package com.crewcrew.global.common.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaginationResponse<T> {
  private List<T> content;
  private int pageNumber;
  private int pageSize;
  private long totalElements;
  private int totalPages;
}
