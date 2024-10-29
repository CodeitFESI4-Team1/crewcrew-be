package com.crewcrew.domain.crew.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

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
}
