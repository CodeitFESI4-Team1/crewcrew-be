package com.crewcrew.domain.crew.mapper;

import java.util.*;

import org.springframework.stereotype.Service;

import com.crewcrew.domain.crew.dto.request.CrewCreateRequestDTO;
import com.crewcrew.domain.crew.dto.response.*;
import com.crewcrew.domain.crew.entity.*;
import com.crewcrew.domain.member.entity.Member;

@Service
public class CrewMapper {

  public Crew toEntity(CrewCreateRequestDTO request, Member member) {
    return Crew.builder()
        .location(request.location())
        .detailedLocation(request.detailedLocation())
        .type(request.type())
        .subType(request.subType())
        .name(request.name())
        .description(request.description())
        .capacity(request.capacity())
        .member(member)
        .build();
  }

  public CrewResponseDTO crewResponseDTO(Crew crew, List<ImageResponseDTO> images) {
    return new CrewResponseDTO(
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
