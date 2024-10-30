package com.crewcrew.domain.crew.dto.response;

import java.util.List;

import com.crewcrew.domain.crew.enums.Category;
import com.crewcrew.domain.crew.enums.SubCategory;

import io.swagger.v3.oas.annotations.media.Schema;

public record CrewResponseDTO(
    @Schema(description = "크루 ID") long crewId,
    @Schema(description = "크루 유형") Category type,
    @Schema(description = "크루 세부 유형") SubCategory subType,
    @Schema(description = "모임 이름") String name,
    @Schema(description = "모임 설명") String description,
    @Schema(description = "모임 장소 (예: 서울특별시)") String location,
    @Schema(description = "상세 위치 (예: 마포구)") String detailedLocation,
    @Schema(description = "참여자 수") int participantCount,
    @Schema(description = "모집 정원") int capacity,
    @Schema(description = "이미지 목록") List<ImageResponseDTO> images,
    @Schema(description = "크루를 생성한 사용자 ID") long createdBy,
    @Schema(description = "개설 확정 여부") boolean isConfirmed) {}
