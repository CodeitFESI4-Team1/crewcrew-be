package com.crewcrew.domain.crew.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.crewcrew.domain.crew.enums.Category;
import com.crewcrew.domain.crew.enums.SubCategory;

public record CrewDetailResponseDTO(
    Long crewId,
    Category type,
    SubCategory subType,
    String name,
    String location,
    String detailedLocation,
    int participantCount,
    int capacity,
    List<ImageResponseDTO> images,
    Long createdBy,
    LocalDateTime createdDate,
    LocalDateTime updatedDate,
    LocalDateTime canceledAt,
    boolean isGathering,
    List<JoinedParticipantDTO> joinedParticipants) {}
