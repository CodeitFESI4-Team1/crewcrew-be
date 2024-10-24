package com.crewcrew.domain.crew.entity;

import jakarta.persistence.*;

import com.crewcrew.domain.crew.enums.ImageType;

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
  private Long id;

  @Column(name = "image_path", nullable = false, length = 500)
  private String imagePath;

  @Enumerated(EnumType.STRING)
  @Column(name = "image_type", nullable = false)
  private ImageType imageType;

  @Column(name = "reference_id", nullable = false)
  private Long referenceId;
}
