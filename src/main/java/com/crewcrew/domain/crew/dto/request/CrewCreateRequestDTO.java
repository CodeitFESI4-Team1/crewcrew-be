package com.crewcrew.domain.crew.dto.request;

import com.crewcrew.domain.crew.enums.Category;
import com.crewcrew.domain.crew.enums.SubCategory;

public record CrewCreateRequestDTO(
    Category type,
    SubCategory subType,
    String name,
    String description,
    String location,
    String detailedLocation,
    int capacity) {}
