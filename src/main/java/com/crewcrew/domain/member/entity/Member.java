package com.crewcrew.domain.member.entity;

import java.time.*;
import java.util.List;

import jakarta.persistence.*;

import com.crewcrew.domain.crew.entity.Crew;
import com.crewcrew.global.common.domain.BaseEntity;

import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_id")
  private Long id;

  @Column(nullable = false, unique = true, length = 255)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String name;

  @Column(name = "deleted_at", nullable = true)
  private LocalDateTime deletedAt;

  @OneToMany(mappedBy = "member")
  private List<Crew> crews;
}
