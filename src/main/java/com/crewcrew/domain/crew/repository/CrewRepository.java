package com.crewcrew.domain.crew.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.crewcrew.domain.crew.entity.Crew;

@Repository
public interface CrewRepository extends JpaRepository<Crew, Long>, CrewCustomRepository {
  boolean existsByTitleAndIdNot(@Param("title") String title, @Param("crewId") Long crewId);
}
