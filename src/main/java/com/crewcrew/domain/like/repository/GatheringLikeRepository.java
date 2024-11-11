package com.crewcrew.domain.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.crewcrew.domain.like.entity.GatheringLike;

@Repository
public interface GatheringLikeRepository extends JpaRepository<GatheringLike, Long> {
  boolean existsByGatheringIdAndMemberId(Long gatheringId, Long memberId);

  @Modifying
  @Query("DELETE FROM GatheringLike gl WHERE gl.gathering.crew.id = :crewId")
  void deleteByGatheringCrewId(@Param("crewId") Long crewId);
}
