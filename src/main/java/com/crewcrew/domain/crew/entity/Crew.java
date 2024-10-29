package com.crewcrew.domain.crew.entity;

import java.time.LocalDateTime;
import java.util.Optional;

import jakarta.persistence.*;

import com.crewcrew.domain.crew.dto.request.CrewUpdateRequestDTO;
import com.crewcrew.domain.crew.enums.Category;
import com.crewcrew.domain.crew.enums.SubCategory;
import com.crewcrew.domain.member.entity.Member;
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

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Category type;

  @Enumerated(EnumType.STRING)
  @Column(name = "sub_type", nullable = false)
  private SubCategory subType;

  @Column(nullable = false)
  private String name;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(nullable = false)
  private String location;

  @Column(name = "detailed_location")
  private String detailedLocation;

  @Column(nullable = false)
  private int capacity;

  @Builder.Default
  @Column(name = "participant_count")
  private int participantCount = 0;

  @Column(name = "canceled_at", nullable = true)
  private LocalDateTime canceledAt;

  @Builder.Default
  @Column(name = "is_confirmed")
  private Boolean isConfirmed = false;

  @ManyToOne
  @JoinColumn(name = "member_id")
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
}
