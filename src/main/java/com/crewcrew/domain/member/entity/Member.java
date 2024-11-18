package com.crewcrew.domain.member.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import com.crewcrew.global.common.domain.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(description = "사용자 ID")
  @Column(name = "member_id")
  private Long id;

  @Schema(description = "사용자 이메일")
  private String email;

  @Schema(description = "사용자 비밀번호")
  private String password;

  @Schema(description = "닉네임")
  private String nickName;

  @Schema(description = "프로필 이미지")
  private String profileImageUrl;

  @Schema(description = "회원탈퇴 일자")
  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  public void update(
      String email,
      String password,
      String nickName,
      String profileImageUrl,
      LocalDateTime deletedAt) {
    this.email = email;
    this.password = password;
    this.nickName = nickName;
    this.profileImageUrl = profileImageUrl;
    this.deletedAt = deletedAt;
  }

  public void resetProfileImageUrl() {
    this.profileImageUrl = null;
  }
}
