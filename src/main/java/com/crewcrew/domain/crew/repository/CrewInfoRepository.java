package com.crewcrew.domain.crew.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.crewcrew.domain.crew.entity.CrewInfo;

import io.lettuce.core.dynamic.annotation.Param;

public interface CrewInfoRepository extends JpaRepository<CrewInfo, Long> {
  @Query("SELECT ci.crew.id FROM CrewInfo ci WHERE ci.member.id = :memberId")
  List<Long> findCrewIdsByMemberId(@Param("memberId") Long memberId);
}
