package com.crewcrew.domain.crew.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.crewcrew.domain.crew.entity.CrewInfo;

public interface CrewInfoRepository extends JpaRepository<CrewInfo, Long> {
  @Query("SELECT ci.crew.id FROM CrewInfo ci WHERE ci.member.id = :memberId")
  List<Long> findCrewIdsByMemberId(Long memberId);

  List<CrewInfo> findByCrewId(Long crewId);

  @Modifying
  @Query("DELETE FROM CrewInfo ci WHERE ci.crew.id = :crewId AND ci.member.id = :memberId")
  void deleteByCrewIdAndMemberId(Long crewId, Long memberId);
}
