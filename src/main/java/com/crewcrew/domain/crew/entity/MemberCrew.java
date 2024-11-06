package com.crewcrew.domain.crew.entity;

import jakarta.persistence.*;

import com.crewcrew.domain.member.entity.Member;
import com.crewcrew.global.common.domain.BaseEntity;

import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberCrew extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_crew_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "crew_id")
  private Crew crew;

  @Builder.Default private boolean isCaptain = false;
}
