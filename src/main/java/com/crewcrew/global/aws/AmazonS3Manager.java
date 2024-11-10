package com.crewcrew.global.aws;

import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.crewcrew.global.config.AmazonConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3Manager {

  private final AmazonS3 amazonS3;
  private final AmazonConfig amazonConfig;

  public String uploadFile(String keyName, MultipartFile file) {
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(file.getSize());
    try {
      amazonS3.putObject(
          new PutObjectRequest(amazonConfig.getBucket(), keyName, file.getInputStream(), metadata));
    } catch (IOException e) {
      log.error("error at AmazonS3Manager uploadFile : {}", (Object) e.getStackTrace());
    }

    return amazonS3.getUrl(amazonConfig.getBucket(), keyName).toString();
  }

  public void deleteFile(String keyName) {
    try {
      if (doesFileExist(keyName)) {
        amazonS3.deleteObject(new DeleteObjectRequest(amazonConfig.getBucket(), keyName));
        log.info("File deleted successfully: {}", keyName);
      } else {
        log.warn("File not found for deletion: {}", keyName);
      }
    } catch (Exception e) {
      log.error("Error at AmazonS3Manager deleteFile : {}", (Object) e.getStackTrace());
      throw new RuntimeException("Failed to delete file from S3", e);
    }
  }

  public boolean doesFileExist(String keyName) {
    return amazonS3.doesObjectExist(amazonConfig.getBucket(), keyName);
  }

  public String generateCrewKeyName(UUID uuid) {
    return amazonConfig.getCrewPath() + '/' + uuid.toString();
  }

  public String generateGatheringKeyName(UUID uuid) {
    return amazonConfig.getGatheringPath() + '/' + uuid.toString();
  }

  public String generateMemberKeyName(UUID uuid) {
    return amazonConfig.getMemberPath() + '/' + uuid.toString();
  }
}
