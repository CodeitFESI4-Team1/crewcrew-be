package com.crewcrew.domain.review.entity;

import jakarta.persistence.*;

import com.crewcrew.domain.crew.entity.Crew;
import com.crewcrew.domain.gathering.entity.Gathering;
import com.crewcrew.domain.member.entity.Member;
import com.crewcrew.global.common.domain.BaseEntity;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Review extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "review_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "crew_id",
      foreignKey =
          @ForeignKey(
              name = "fk_review_crew",
              foreignKeyDefinition =
                  "FOREIGN KEY (crew_id) REFERENCES crew(crew_id) ON DELETE SET NULL"))
  private Crew crew;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "gathering_id",
      foreignKey =
          @ForeignKey(
              name = "fk_review_gathering",
              foreignKeyDefinition =
                  "FOREIGN KEY (gathering_id) REFERENCES gathering(gathering_id) ON DELETE SET NULL"))
  private Gathering gathering;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @Column(nullable = false)
  private Long rate;

  @Column(nullable = false)
  private String comment;
}
