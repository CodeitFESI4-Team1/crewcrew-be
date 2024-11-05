package com.crewcrew.domain.crew.entity;

import java.time.LocalDateTime;
import java.util.Optional;

import jakarta.persistence.*;

import com.crewcrew.domain.crew.dto.request.CrewUpdateRequestDTO;
import com.crewcrew.domain.crew.enums.Category;
import com.crewcrew.domain.crew.enums.SubCategory;
import com.crewcrew.domain.member.entity.Member;
import com.crewcrew.global.common.domain.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;
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
  @Schema(description = "크루 ID", example = "1")
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Schema(description = "카테고리 (대분류)", example = "CARDIO_STRENGTH")
  private Category type;

  @Enumerated(EnumType.STRING)
  @Column(name = "sub_type", nullable = false)
  @Schema(description = "세부 카테고리", example = "GYM")
  private SubCategory subType;

  @Column(nullable = false)
  @Schema(description = "크루 이름", example = "주말 운동 모임")
  private String name;

  @Column(columnDefinition = "TEXT")
  @Schema(description = "크루 설명", example = "주말마다 운동하는 모임입니다.")
  private String description;

  @Column(nullable = false)
  @Schema(description = "크루 장소", example = "서울특별시")
  private String location;

  @Column(name = "detailed_location")
  @Schema(description = "상세 위치", example = "마포구")
  private String detailedLocation;

  @Column(nullable = false)
  @Schema(description = "크루 정원", example = "12")
  private int capacity;

  @Builder.Default
  @Column(name = "participant_count")
  @Schema(description = "참여자 수", example = "0")
  private int participantCount = 0;

  @Column(name = "canceled_at", nullable = true)
  @Schema(description = "취소일", example = "2024-10-29T20:05:19.639393")
  private LocalDateTime canceledAt;

  @Builder.Default
  @Column(name = "is_confirmed")
  @Schema(description = "개설 확정 여부", example = "false")
  private Boolean isConfirmed = false;

  @ManyToOne
  @JoinColumn(name = "member_id")
  @Schema(description = "주최자 정보")
  private Member member;

  public void update(CrewUpdateRequestDTO request) {
    Optional.ofNullable(request.location()).ifPresent(location -> this.location = location);
    Optional.ofNullable(request.detailedLocation())
        .ifPresent(detailedLocation -> this.detailedLocation = detailedLocation);
    Optional.ofNullable(request.type()).ifPresent(type -> this.type = Category.valueOf(type));
    Optional.ofNullable(request.subType())
        .ifPresent(subType -> this.subType = SubCategory.valueOf(subType));
    Optional.ofNullable(request.name()).ifPresent(name -> this.name = name);
    Optional.ofNullable(request.description())
        .ifPresent(description -> this.description = description);
    Optional.ofNullable(request.capacity()).ifPresent(capacity -> this.capacity = capacity);
  }

  public void incrementParticipantCount() {
    if (this.participantCount < this.capacity) {
      this.participantCount++;
    }
    if (this.participantCount > 4) {
      this.isConfirmed = true;
    }
  }

  public void decrementParticipantCount() {
    if (participantCount > 0) {
      participantCount--;
      if (participantCount < 4) {
        this.isConfirmed = false;
      }
    }
  }

  public void cancel() {
    this.canceledAt = LocalDateTime.now();
    this.isConfirmed = false;
  }
}
