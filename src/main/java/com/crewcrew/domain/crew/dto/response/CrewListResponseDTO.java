package com.crewcrew.domain.crew.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.crewcrew.domain.crew.enums.Category;
import com.crewcrew.domain.crew.enums.SubCategory;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "크루 리스트 응답 DTO")
public record CrewListResponseDTO(
    @Schema(description = "크루 ID") Long crewId,
    @Schema(description = "크루 유형") Category type,
    @Schema(description = "크루 세부 유형") SubCategory subType,
    @Schema(description = "크루 이름") String name,
    @Schema(description = "크루 설명") String description,
    @Schema(description = "위치") String location,
    @Schema(description = "상세 위치") String detailedLocation,
    @Schema(description = "참여자 수") int participantCount,
    @Schema(description = "크루 정원") int capacity,
    @Schema(description = "이미지 목록") List<ImageResponseDTO> images,
    @Schema(description = "생성자 ID") Long createdBy,
    @Schema(description = "생성일") LocalDateTime createdDate,
    @Schema(description = "수정일") LocalDateTime updatedDate,
    @Schema(description = "취소일") LocalDateTime canceledAt,
    @Schema(description = "개설 확정 여부") boolean isConfirmed) {}
