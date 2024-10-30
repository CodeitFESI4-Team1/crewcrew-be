package com.crewcrew.domain.crew.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record JoinedParticipantDTO(
    @Schema(description = "사용자 고유 ID") Long memberId,
    @Schema(description = "사용자 이름") String memberName) {}
