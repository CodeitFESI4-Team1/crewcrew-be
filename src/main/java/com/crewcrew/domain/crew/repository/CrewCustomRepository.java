package com.crewcrew.domain.crew.repository;

import java.util.Optional;

import com.crewcrew.domain.crew.dto.response.CrewDetailResponse;

public interface CrewCustomRepository {
  boolean existsByTitleIgnoreCaseAndSpace(String title);

  Optional<CrewDetailResponse> findCrewDetailById(Long crewId);
}
