package com.crewcrew.domain.crew.mapper;

import java.util.*;

import org.springframework.stereotype.Service;

import com.crewcrew.domain.crew.dto.response.*;
import com.crewcrew.domain.crew.entity.*;

@Service
public class CrewMapper {

  public CrewResponseDTO crewResponseDTO(Crew crew, List<ImageResponseDTO> images) {
    return new CrewResponseDTO(
        crew.getId(),
        crew.getType(),
        crew.getSubType(),
        crew.getName(),
        crew.getLocation(),
        crew.getDetailedLocation(),
        crew.getParticipantCount(),
        crew.getCapacity(),
        images,
        crew.getMember().getId());
  }

  public CrewListResponseDTO crewListResponseDTO(Crew crew, List<ImageResponseDTO> images) {
    return new CrewListResponseDTO(
        crew.getId(),
        crew.getType(),
        crew.getSubType(),
        crew.getName(),
        crew.getDescription(),
        crew.getLocation(),
        crew.getDetailedLocation(),
        crew.getParticipantCount(),
        crew.getCapacity(),
        images,
        crew.getMember().getId(),
        crew.getCreatedAt(),
        crew.getUpdatedAt(),
        crew.getCanceledAt(),
        crew.getIsConfirmed());
  }

  public CrewDetailResponseDTO crewDetailResponseDTO(
      Crew crew, List<ImageResponseDTO> images, List<JoinedParticipantDTO> participants) {
    return new CrewDetailResponseDTO(
        crew.getId(),
        crew.getType(),
        crew.getSubType(),
        crew.getName(),
        crew.getLocation(),
        crew.getDetailedLocation(),
        crew.getParticipantCount(),
        crew.getCapacity(),
        images,
        crew.getMember().getId(),
        crew.getCreatedAt(),
        crew.getUpdatedAt(),
        crew.getCanceledAt(),
        crew.getIsConfirmed(),
        participants);
  }
}
