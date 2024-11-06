package com.crewcrew.domain.crew.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.crewcrew.domain.crew.dto.response.CrewDetailResponse;
import com.crewcrew.domain.crew.dto.response.JoinedCrewResponse;

public interface CrewCustomRepository {
  boolean existsByTitleIgnoreCaseAndSpace(String title);

  Optional<CrewDetailResponse> findCrewDetailById(Long crewId);

  Slice<JoinedCrewResponse> findJoinedCrews(String email, Pageable pageable);
}
