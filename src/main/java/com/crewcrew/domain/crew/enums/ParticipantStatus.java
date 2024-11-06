package com.crewcrew.domain.crew.enums;

import lombok.Getter;

@Getter
public enum ParticipantStatus {
  PARTICIPATING("참여중"),
  CANCELLED("취소됨");

  private final String description;

  ParticipantStatus(String description) {
    this.description = description;
  }
}
