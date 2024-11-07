package com.crewcrew.domain.crew.enums;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;

@Getter
public enum SubCategory {
  // 유산소/근력
  RUNNING("러닝", "running", MainCategory.CARDIO_STRENGTH),
  CYCLING("자전거", "cycling", MainCategory.CARDIO_STRENGTH),
  HIKING("등산", "hiking", MainCategory.CARDIO_STRENGTH),
  SWIMMING("수영", "swimming", MainCategory.CARDIO_STRENGTH),
  GYM("헬스", "gym", MainCategory.CARDIO_STRENGTH),
  CLIMBING("클라이밍", "climbing", MainCategory.CARDIO_STRENGTH),

  // 유연성
  YOGA("요가", "yoga", MainCategory.FLEXIBILITY),
  PILATES("필라테스", "pilates", MainCategory.FLEXIBILITY),
  POLE_DANCE("폴댄스", "pole_dance", MainCategory.FLEXIBILITY),
  BALLET("발레", "ballet", MainCategory.FLEXIBILITY),

  // 구기 종목
  SOCCER("축구", "soccer", MainCategory.BALL_SPORTS),
  TENNIS("테니스", "tennis", MainCategory.BALL_SPORTS),
  BADMINTON("배드민턴", "badminton", MainCategory.BALL_SPORTS),
  BOWLING("볼링", "bowling", MainCategory.BALL_SPORTS),
  TABLE_TENNIS("탁구", "table_tennis", MainCategory.BALL_SPORTS),
  BASKETBALL("농구", "basketball", MainCategory.BALL_SPORTS),
  BASEBALL("야구", "baseball", MainCategory.BALL_SPORTS),
  VOLLEYBALL("배구", "volleyball", MainCategory.BALL_SPORTS),
  GOLF("골프", "golf", MainCategory.BALL_SPORTS),

  // 기타
  SURFING("서핑", "surfing", MainCategory.OTHER_SPORTS),
  SKIING("스키", "skiing", MainCategory.OTHER_SPORTS),
  SNOWBOARDING("스노보드", "snowboarding", MainCategory.OTHER_SPORTS),
  SCUBA_DIVING("스쿠버다이빙", "scuba_diving", MainCategory.OTHER_SPORTS),
  ICE_SKATING("아이스스케이트", "ice_skating", MainCategory.OTHER_SPORTS);

  private final String label;
  @Getter private final String value;
  @Getter private final MainCategory mainCategory;

  SubCategory(String label, String value, MainCategory mainCategory) {
    this.label = label;
    this.value = value;
    this.mainCategory = mainCategory;
  }

  @JsonValue
  public String getLabel() {
    return label;
  }

  public static SubCategory fromValue(String value) {
    return Arrays.stream(values())
        .filter(category -> category.getValue().equals(value))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("잘못된 SubCategory value: " + value));
  }

  public static SubCategory fromLabel(String label) {
    return Arrays.stream(values())
        .filter(category -> category.getLabel().equals(label))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("잘못된 SubCategory label: " + label));
  }
}
