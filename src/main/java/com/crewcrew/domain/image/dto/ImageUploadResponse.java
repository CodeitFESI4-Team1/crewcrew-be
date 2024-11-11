package com.crewcrew.domain.image.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ImageUploadResponse {
  private String imageUrl;

  public static ImageUploadResponse from(String imageUrl) {
    return ImageUploadResponse.builder().imageUrl(imageUrl).build();
  }
}
