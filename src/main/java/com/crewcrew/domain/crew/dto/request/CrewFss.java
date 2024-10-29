package com.crewcrew.domain.crew.dto.request;

import lombok.Builder;

@Builder
public record CrewFss(
    String subType, String type, String location, String detailedLocation, String searchKeyword) {}
