package com.crewcrew.domain.crew.dto.response;

import java.util.List;

import com.crewcrew.domain.crew.enums.Category;
import com.crewcrew.domain.crew.enums.SubCategory;

public record CrewResponseDTO(
    long crewId,
    Category type,
    SubCategory subType,
    String name,
    String description,
    String location,
    String detailedLocation,
    int participantCount,
    int capacity,
    List<ImageResponseDTO> images,
    long createdBy) {}
