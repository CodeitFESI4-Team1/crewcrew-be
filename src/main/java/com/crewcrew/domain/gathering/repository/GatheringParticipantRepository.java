package com.crewcrew.domain.gathering.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.crewcrew.domain.gathering.entity.Gathering;
import com.crewcrew.domain.gathering.entity.GatheringParticipant;
import com.crewcrew.domain.member.entity.Member;

@Repository
public interface GatheringParticipantRepository extends JpaRepository<GatheringParticipant, Long> {
  List<GatheringParticipant> findByGatheringId(Long gatheringId);

  boolean existsByGatheringIdAndMemberId(Long gatheringId, Long memberId);

  long countByGatheringId(Long gatheringId);

  @Modifying
  @Query("DELETE FROM GatheringParticipant gp WHERE gp.gathering.crew.id = :crewId")
  void deleteByGatheringCrewId(@Param("crewId") Long crewId);

  @Modifying
  @Query(
      "DELETE FROM GatheringParticipant gp WHERE gp.gathering.crew.id = :crewId AND gp.member.id = :memberId")
  void deleteByCrewIdAndMemberId(@Param("crewId") Long crewId, @Param("memberId") Long memberId);

  Optional<GatheringParticipant> findByGatheringAndMember(Gathering gathering, Member member);

  @Modifying
  @Query("DELETE FROM GatheringLike gl WHERE gl.gathering = :gathering")
  void deleteByGathering(@Param("gathering") Gathering gathering);
}
