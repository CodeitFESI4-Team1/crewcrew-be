package com.crewcrew.domain.crew.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record CrewUpdateRequestDTO(
    @Schema(description = "크루 장소") String location,
    @Schema(description = "상세 위치") String detailedLocation,
    @Schema(description = "크루 유형") String type,
    @Schema(description = "크루 세부 유형") String subType,
    @Schema(description = "크루 이름") String name,
    @Schema(description = "크루 설명") String description,
    @Schema(description = "크루 정원") Integer capacity) {}
