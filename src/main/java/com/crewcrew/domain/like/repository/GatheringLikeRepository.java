package com.crewcrew.domain.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crewcrew.domain.like.entity.GatheringLike;

@Repository
public interface GatheringLikeRepository extends JpaRepository<GatheringLike, Long> {
  boolean existsByGatheringIdAndMemberId(Long gatheringId, Long memberId);
}
