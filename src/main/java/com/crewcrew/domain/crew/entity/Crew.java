package com.crewcrew.domain.crew.entity;

import jakarta.persistence.*;

import com.crewcrew.domain.crew.enums.MainCategory;
import com.crewcrew.domain.crew.enums.SubCategory;
import com.crewcrew.global.common.domain.BaseEntity;

import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Crew extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "crew_id")
  private Long id;

  @Column(length = 20, nullable = false, unique = true)
  private String title;

  @Column(length = 100)
  private String introduce;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private MainCategory mainCategory;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private SubCategory subCategory;

  @Column(nullable = false)
  private String mainLocation;

  @Column private String subLocation;

  @Column(nullable = false)
  private Integer totalCount;

  @Column(nullable = false)
  private String imageUrl;

  @Builder.Default private boolean isConfirmed = false;

  public void updateImage(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public void update(
      String title,
      String introduce,
      MainCategory mainCategory,
      SubCategory subCategory,
      String mainLocation,
      String subLocation,
      Integer totalCount,
      String imageUrl) {
    this.title = title;
    this.introduce = introduce;
    this.mainCategory = mainCategory;
    this.subCategory = subCategory;
    this.mainLocation = mainLocation;
    this.subLocation = subLocation;
    this.totalCount = totalCount;
    this.imageUrl = imageUrl;
  }
}
