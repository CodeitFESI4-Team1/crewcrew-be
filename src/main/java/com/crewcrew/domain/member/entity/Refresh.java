package com.crewcrew.domain.member.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import com.crewcrew.global.common.domain.BaseEntity;

import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Refresh extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_id")
  private Long id;

  private Long userId;
  private String refreshToken;
  private LocalDateTime expiredAt;
}
