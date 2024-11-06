package com.crewcrew.domain.gathering;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import com.crewcrew.domain.crew.entity.Crew;
import com.crewcrew.domain.member.entity.Member;
import com.crewcrew.global.common.domain.BaseEntity;

import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Gathering extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "gathering_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "crew_id")
  private Crew crew;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "captain_id")
  private Member captain;

  @Column(length = 20, nullable = false)
  private String title;

  @Column(length = 100)
  private String introduce;

  @Column(nullable = false)
  private LocalDateTime dateTime;

  @Column(nullable = false)
  private String location;

  @Column(nullable = false)
  private Integer totalCount;

  @Column(nullable = false)
  private String imageUrl;

  public void updateImage(String imageUrl) {
    this.imageUrl = imageUrl;
  }
}
