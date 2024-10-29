package com.crewcrew.domain.crew.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crewcrew.domain.crew.entity.Crew;

@Repository
public interface CrewRepository extends JpaRepository<Crew, Long>, CrewCustomRepository {
  boolean existsByIdAndMemberId(Long crewId, Long memberId);
}
