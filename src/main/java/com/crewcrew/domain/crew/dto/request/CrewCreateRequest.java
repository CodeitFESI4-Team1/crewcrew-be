package com.crewcrew.domain.crew.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.crewcrew.domain.crew.entity.Crew;
import com.crewcrew.domain.crew.enums.MainCategory;
import com.crewcrew.domain.crew.enums.SubCategory;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CrewCreateRequest {

  @NotBlank(message = "제목은 필수입니다")
  @Size(max = 20, message = "제목은 20자를 초과할 수 없습니다")
  private String title;

  @NotNull(message = "메인 카테고리는 필수입니다")
  private String mainCategory;

  @NotNull(message = "서브 카테고리는 필수입니다")
  private String subCategory;

  @NotBlank(message = "메인 지역은 필수입니다")
  private String mainLocation;

  @NotBlank(message = "서브 지역은 필수입니다")
  private String subLocation;

  @NotNull(message = "총 인원은 필수입니다")
  @Min(value = 1, message = "총 인원은 1명 이상이어야 합니다")
  private Integer totalCount;

  @NotBlank(message = "이미지 URL은 필수입니다")
  private String imageUrl;

  public Crew toEntity() {
    return Crew.builder()
        .title(title)
        .mainCategory(MainCategory.fromValue(mainCategory))
        .subCategory(SubCategory.fromValue(subCategory))
        .mainLocation(mainLocation)
        .subLocation(subLocation)
        .totalCount(totalCount)
        .imageUrl(imageUrl)
        .isConfirmed(false)
        .build();
  }
}
