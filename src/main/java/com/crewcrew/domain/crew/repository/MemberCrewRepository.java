package com.crewcrew.domain.crew.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crewcrew.domain.crew.entity.MemberCrew;

public interface MemberCrewRepository extends JpaRepository<MemberCrew, Long> {

  boolean existsByCrewIdAndMemberEmailAndIsCaptainIsTrue(Long crewId, String email);

  long countByCrewId(Long crewId);

  boolean existsByCrewIdAndMemberId(Long crewId, Long memberId);

  Optional<MemberCrew> findByCrewIdAndMemberId(Long crewId, Long id);
}
