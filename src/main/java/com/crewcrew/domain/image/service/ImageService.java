package com.crewcrew.domain.image.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.crewcrew.domain.image.entity.ImageType;
import com.crewcrew.global.aws.AmazonS3Manager;
import com.crewcrew.global.common.exception.ApiException;
import com.crewcrew.global.common.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService {
  private final AmazonS3Manager s3Manager;

  public String uploadImage(ImageType type, MultipartFile file) {
    validateImageFile(file);

    String keyName =
        switch (type) {
          case MEMBER -> s3Manager.generateMemberKeyName(UUID.randomUUID());
          case CREW -> s3Manager.generateCrewKeyName(UUID.randomUUID());
          case GATHERING -> s3Manager.generateGatheringKeyName(UUID.randomUUID());
        };

    return s3Manager.uploadFile(keyName, file);
  }

  private void validateImageFile(MultipartFile file) {
    if (file == null || file.isEmpty()) {
      throw new ApiException(ErrorCode.IMAGE_REQUIRED);
    }

    if (file.getSize() > 5 * 1024 * 1024) {
      throw new ApiException(ErrorCode.IMAGE_SIZE_EXCEEDED);
    }

    String contentType = file.getContentType();
    if (contentType == null || !isValidImageType(contentType)) {
      throw new ApiException(ErrorCode.INVALID_IMAGE_TYPE);
    }
  }

  private boolean isValidImageType(String contentType) {
    return contentType.equals("image/jpeg")
        || contentType.equals("image/jpg")
        || contentType.equals("image/png");
  }
}
