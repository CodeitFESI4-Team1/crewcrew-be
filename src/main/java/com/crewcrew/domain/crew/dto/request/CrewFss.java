package com.crewcrew.domain.crew.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record CrewFss(
    @Schema(description = "세부 카테고리") String subType,
    @Schema(description = "카테고리 (대분류 카테고리)") String type,
    @Schema(description = "크루 장소 (예: 서울특볍ㄹ시)") String location,
    @Schema(description = "상세 위치 (예: 마포구)") String detailedLocation,
    @Schema(description = "크루 이름 검색 키워드") String searchKeyword) {}
