package com.crewcrew.domain.member.entity;

import java.time.*;
import java.util.List;

import jakarta.persistence.*;

import com.crewcrew.domain.crew.entity.Crew;
import com.crewcrew.global.common.domain.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_id")
  @Schema(description = "사용자 ID")
  private Long id;

  @Column(nullable = false, unique = true, length = 255)
  @Schema(description = "사용자 이메일", required = true)
  private String email;

  @Column(nullable = false)
  @Schema(description = "사용자 비밀번호", required = true)
  private String password;

  @Column(nullable = false)
  @Schema(description = "사용자 이름", required = true)
  private String name;

  @Column(name = "deleted_at", nullable = true)
  @Schema(description = "회원탈퇴 일자")
  private LocalDateTime deletedAt;

  @OneToMany(mappedBy = "member")
  @Schema(description = "사용자가 참여한 크루 목록")
  private List<Crew> crews;
}
