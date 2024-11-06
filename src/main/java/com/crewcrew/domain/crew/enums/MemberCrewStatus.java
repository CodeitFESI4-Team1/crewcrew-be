package com.crewcrew.domain.crew.enums;

import lombok.Getter;

@Getter
public enum MemberCrewStatus {
  JOINED("활성"),
  LEFT("탈퇴");

  private final String description;

  MemberCrewStatus(String description) {
    this.description = description;
  }
}
