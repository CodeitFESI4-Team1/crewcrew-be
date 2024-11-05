package com.crewcrew.domain.crew.dto.request;

import com.crewcrew.domain.crew.enums.Category;
import com.crewcrew.domain.crew.enums.SubCategory;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "크루 생성 요청 DTO")
public record CrewCreateRequestDTO(
    @Schema(description = "크루 유형", required = true) Category type,
    @Schema(description = "크루 세부 유형", required = true) SubCategory subType,
    @Schema(description = "크루 이름", required = true) String name,
    @Schema(description = "크루 설명", required = true) String description,
    @Schema(description = "크루 장소", required = true) String location,
    @Schema(description = "상세 위치", required = true) String detailedLocation,
    @Schema(description = "모집 정원", required = true) int capacity) {}
