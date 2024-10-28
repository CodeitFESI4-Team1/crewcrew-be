package com.crewcrew.domain.crew.dto.request;

public record CrewUpdateRequestDTO(
    String location,
    String detailedLocation,
    String type,
    String subType,
    String name,
    String description,
    Integer capacity) {}
