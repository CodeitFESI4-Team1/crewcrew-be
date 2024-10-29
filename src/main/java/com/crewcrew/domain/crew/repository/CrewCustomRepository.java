package com.crewcrew.domain.crew.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.crewcrew.domain.crew.dto.request.CrewFss;
import com.crewcrew.domain.crew.entity.Crew;

public interface CrewCustomRepository {

  Slice<Crew> findFilteredCrews(CrewFss fss, Pageable pageable);
}
