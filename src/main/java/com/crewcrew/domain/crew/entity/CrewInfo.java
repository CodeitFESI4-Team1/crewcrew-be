package com.crewcrew.domain.crew.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import com.crewcrew.domain.member.entity.Member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Table(name = "crew_info")
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CrewInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(description = "크루 정보 ID", example = "1")
  private Long crewInfoId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "crew_id", nullable = false)
  @Schema(description = "참여한 크루", required = true)
  private Crew crew;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  @Schema(description = "참여한 회원", required = true)
  private Member member;

  @Column(name = "joined_at", nullable = false)
  @Schema(description = "참여 일시", example = "2024-10-29T20:05:19.639393")
  private LocalDateTime joinedAt;

  @PrePersist
  protected void onCreate() {
    this.joinedAt = LocalDateTime.now();
  }
}
