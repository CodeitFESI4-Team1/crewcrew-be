package com.crewcrew.domain.crew.enums;

import java.util.Arrays;
import java.util.List;

import com.crewcrew.global.common.exception.ApiException;
import com.crewcrew.global.common.exception.ErrorCode;

import lombok.Getter;

@Getter
public enum MainCategory {
  CARDIO_STRENGTH(
      "유산소/근력",
      "cardio_strength",
      Arrays.asList(
          SubCategory.RUNNING,
          SubCategory.CYCLING,
          SubCategory.HIKING,
          SubCategory.SWIMMING,
          SubCategory.GYM,
          SubCategory.CLIMBING)),

  FLEXIBILITY(
      "유연성",
      "flexibility",
      Arrays.asList(
          SubCategory.YOGA, SubCategory.PILATES, SubCategory.POLE_DANCE, SubCategory.BALLET)),

  BALL_SPORTS(
      "구기 종목",
      "ball_sports",
      Arrays.asList(
          SubCategory.SOCCER,
          SubCategory.TENNIS,
          SubCategory.BADMINTON,
          SubCategory.BOWLING,
          SubCategory.TABLE_TENNIS,
          SubCategory.BASKETBALL,
          SubCategory.BASEBALL,
          SubCategory.VOLLEYBALL,
          SubCategory.GOLF)),

  OTHER_SPORTS(
      "기타",
      "other_sports",
      Arrays.asList(
          SubCategory.SURFING,
          SubCategory.SKIING,
          SubCategory.SNOWBOARDING,
          SubCategory.SCUBA_DIVING,
          SubCategory.ICE_SKATING));

  private final String label;
  private final String value;
  private final List<SubCategory> subCategories;

  MainCategory(String label, String value, List<SubCategory> subCategories) {
    this.label = label;
    this.value = value;
    this.subCategories = subCategories;
  }

  public static MainCategory fromValue(String value) {
    return Arrays.stream(values())
        .filter(category -> category.getValue().equals(value))
        .findFirst()
        .orElseThrow(() -> new ApiException(ErrorCode.INVALID_MAIN_CATEGORY));
  }

  public static MainCategory fromLabel(String label) {
    return Arrays.stream(values())
        .filter(category -> category.getLabel().equals(label))
        .findFirst()
        .orElseThrow(() -> new ApiException(ErrorCode.INVALID_MAIN_CATEGORY));
  }
}
