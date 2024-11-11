package com.crewcrew.domain.image.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ImageType {
  MEMBER("프로필 이미지"),
  CREW("크루 이미지"),
  GATHERING("모임 이미지");

  private final String description;
}
