package com.crewcrew.domain.image.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.crewcrew.domain.image.dto.ImageUploadResponse;
import com.crewcrew.domain.image.entity.ImageType;
import com.crewcrew.domain.image.service.ImageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
@Tag(name = "이미지 API", description = "이미지 업로드 API")
public class ImageController {
  private final ImageService imageService;

  @Operation(summary = "이미지 업로드", description = "멤버/크루/모임에 사용될 이미지를 업로드합니다.")
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ImageUploadResponse> uploadImage(
      @Parameter(description = "이미지 타입(MEMBER/CREW/GATHERING)", required = true)
          @RequestParam("type")
          ImageType type,
      @Parameter(description = "이미지 파일(JPEG/JPG/PNG)", required = true) @RequestParam("file")
          MultipartFile file) {

    String imageUrl = imageService.uploadImage(type, file);
    return ResponseEntity.ok(ImageUploadResponse.from(imageUrl));
  }
}
