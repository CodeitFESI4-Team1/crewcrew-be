package com.crewcrew.domain.gathering.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crewcrew.domain.gathering.entity.Gathering;

@Repository
public interface GatheringRepository extends JpaRepository<Gathering, Long> {
  Optional<Gathering> findByIdAndCrewId(Long id, Long crewId);

  boolean existsByCrewIdAndDateTime(Long crewId, LocalDateTime dateTime);
}
