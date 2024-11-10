package com.crewcrew.domain.gathering.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crewcrew.domain.gathering.entity.GatheringParticipant;

@Repository
public interface GatheringParticipantRepository extends JpaRepository<GatheringParticipant, Long> {
  List<GatheringParticipant> findByGatheringId(Long gatheringId);

  boolean existsByGatheringIdAndMemberId(Long gatheringId, Long memberId);

  long countByGatheringId(Long gatheringId);
}
