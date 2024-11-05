package com.crewcrew.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crewcrew.domain.member.entity.Refresh;

public interface RefreshRepository extends JpaRepository<Refresh, Long> {

  Boolean existsByRefreshToken(String refreshToken);

  void deleteByRefreshToken(String refreshToken);
}
