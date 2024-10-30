package com.crewcrew.domain.crew.entity;

import jakarta.persistence.*;

import com.crewcrew.domain.crew.enums.ImageType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Table(name = "images")
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(description = "이미지 ID")
  private Long id;

  @Column(name = "image_path", nullable = false, length = 500)
  @Schema(description = "이미지 경로", required = true)
  private String imagePath;

  @Enumerated(EnumType.STRING)
  @Column(name = "image_type", nullable = false)
  @Schema(description = "이미지 타입", required = true)
  private ImageType imageType;

  @Column(name = "reference_id", nullable = false)
  @Schema(description = "참조 ID (크루ID 또는 모임 ID)", required = true)
  private Long referenceId;
}
