package com.crewcrew.domain.crew.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record ImageResponseDTO(@Schema(description = "이미지 경로") String imagePath) {}
