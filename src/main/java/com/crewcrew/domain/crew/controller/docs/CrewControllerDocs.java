package com.crewcrew.domain.crew.controller.docs;

import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.crewcrew.domain.crew.dto.request.*;
import com.crewcrew.domain.crew.dto.response.*;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "크루 기능 API")
public interface CrewControllerDocs {

  @Operation(summary = "크루 생성", description = "새로운 크루를 생성합니다.")
  @ApiResponse(
      responseCode = "201",
      description = "크루 생성 성공",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = CrewResponseDTO.class)))
  @ApiResponse(
      responseCode = "401",
      description = "인증 오류",
      content =
          @Content(
              mediaType = "application/json",
              schema =
                  @Schema(
                      example =
                          "{\"code\": \"UNAUTHORIZED\", \"message\": \"Authorization 헤더가 필요합니다\"}")))
  ResponseEntity<CrewResponseDTO> createCrew(
      @Parameter(description = "크루 생성 요청 DTO", required = true) @RequestBody
          CrewCreateRequestDTO request);

  @Operation(summary = "크루 조회", description = "특정 조건에 맞는 크루 리스트를 조회합니다.")
  @ApiResponse(
      responseCode = "200",
      description = "크루 조회 성공",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = CrewListResponseDTO.class)))
  ResponseEntity<Slice<CrewListResponseDTO>> getCrew(
      @Parameter(description = "크루 필터링 조건", required = false) CrewFss fss,
      @Parameter(description = "페이지 정보", required = false)
          @PageableDefault(size = 10, page = 0, sort = "updatedAt", direction = Sort.Direction.DESC)
          Pageable pageable);

  @Operation(summary = "주최자가 만든 크루 조회", description = "주최자가 만든 크루 리스트를 조회합니다.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "크루 조회 성공",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CrewListResponseDTO.class))),
    @ApiResponse(
        responseCode = "401",
        description = "인증 오류",
        content =
            @Content(
                mediaType = "application/json",
                schema =
                    @Schema(
                        example =
                            "{\"code\": \"UNAUTHORIZED\", \"message\": \"Authorization 헤더가 없습니다\"}")))
  })
  ResponseEntity<Slice<CrewListResponseDTO>> getCreatedCrew(
      @Parameter(description = "페이지 정보 (기본값: 0 페이지, 5 크기)", required = false)
          @PageableDefault(size = 5, page = 0, sort = "updatedAt", direction = Sort.Direction.DESC)
          Pageable pageable);

  @Operation(summary = "사용자가 참석한 크루 조회", description = "사용자가 참석한 크루 리스트를 조회합니다.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "크루 조회 성공",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CrewListResponseDTO.class))),
    @ApiResponse(
        responseCode = "401",
        description = "인증 오류",
        content =
            @Content(
                mediaType = "application/json",
                schema =
                    @Schema(
                        example =
                            "{\"code\": \"UNAUTHORIZED\", \"message\": \"Authorization 헤더가 없습니다\"}")))
  })
  ResponseEntity<Slice<CrewListResponseDTO>> getJoinedCrew(
      @Parameter(description = "페이지 정보 (기본값: 0 페이지, 5 크기)", required = false)
          @PageableDefault(size = 5, page = 0, sort = "updatedAt", direction = Sort.Direction.DESC)
          Pageable pageable);

  @Operation(summary = "크루 상세 조회", description = "특정 크루의 세부 정보를 조회합니다.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "크루 상세 조회 성공",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CrewDetailResponseDTO.class))),
    @ApiResponse(
        responseCode = "404",
        description = "크루를 찾을 수 없음",
        content =
            @Content(
                mediaType = "application/json",
                schema =
                    @Schema(example = "{\"code\": \"NOT_FOUND\", \"message\": \"크루를 찾을 수 없습니다\"}")))
  })
  ResponseEntity<CrewDetailResponseDTO> getCrewDetails(
      @Parameter(description = "크루 ID", required = true) @PathVariable Long id);

  @Operation(summary = "크루 수정", description = "크루 정보를 수정합니다.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "크루 수정 성공",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CrewResponseDTO.class))),
    @ApiResponse(
        responseCode = "403",
        description = "권한 없음",
        content =
            @Content(
                mediaType = "application/json",
                schema =
                    @Schema(
                        example = "{\"code\": \"FORBIDDEN\", \"message\": \"크루를 수정할 권한이 없습니다\"}"))),
    @ApiResponse(
        responseCode = "404",
        description = "크루를 찾을 수 없음",
        content =
            @Content(
                mediaType = "application/json",
                schema =
                    @Schema(
                        example = "{\"code\": \"NOT_FOUND\", \"message\": \"크루를 찾을 수 없습니다\"}"))),
    @ApiResponse(
        responseCode = "401",
        description = "인증 오류",
        content =
            @Content(
                mediaType = "application/json",
                schema =
                    @Schema(
                        example =
                            "{\"code\": \"UNAUTHORIZED\", \"message\": \"Authorization 헤더가 없습니다\"}")))
  })
  ResponseEntity<CrewResponseDTO> updateCrew(
      @Parameter(description = "크루 ID", required = true) @PathVariable Long id,
      @Parameter(description = "크루 수정 요청 DTO", required = true) @RequestBody
          CrewUpdateRequestDTO request);

  @Operation(summary = "크루 참여", description = "특정 크루에 참여합니다.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "201",
        description = "크루 참여 성공",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Void.class))),
    @ApiResponse(
        responseCode = "401",
        description = "인증 오류",
        content =
            @Content(
                mediaType = "application/json",
                schema =
                    @Schema(
                        example =
                            "{\"code\": \"UNAUTHORIZED\", \"message\": \"Authorization 헤더가 필요합니다\"}"))),
    @ApiResponse(
        responseCode = "404",
        description = "크루를 찾을 수 없음",
        content =
            @Content(
                mediaType = "application/json",
                schema =
                    @Schema(example = "{\"code\": \"NOT_FOUND\", \"message\": \"크루를 찾을 수 없습니다\"}")))
  })
  ResponseEntity<Void> joinCrew(
      @Parameter(description = "크루 ID", required = true) @PathVariable Long id);

  @Operation(summary = "주최자 크루 취소", description = "주최자가 특정 크루를 취소합니다.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "크루 취소 성공",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CrewResponseDTO.class))),
    @ApiResponse(
        responseCode = "401",
        description = "인증 오류",
        content =
            @Content(
                mediaType = "application/json",
                schema =
                    @Schema(
                        example =
                            "{\"code\": \"UNAUTHORIZED\", \"message\": \"Authorization 헤더가 필요합니다\"}"))),
    @ApiResponse(
        responseCode = "403",
        description = "권한 없음",
        content =
            @Content(
                mediaType = "application/json",
                schema =
                    @Schema(
                        example = "{\"code\": \"FORBIDDEN\", \"message\": \"크루를 취소할 권한이 없습니다\"}"))),
    @ApiResponse(
        responseCode = "404",
        description = "크루를 찾을 수 없음",
        content =
            @Content(
                mediaType = "application/json",
                schema =
                    @Schema(example = "{\"code\": \"NOT_FOUND\", \"message\": \"크루를 찾을 수 없습니다\"}")))
  })
  ResponseEntity<CrewResponseDTO> cancelCrew(
      @Parameter(description = "크루 ID", required = true) @PathVariable Long id);

  @Operation(summary = "사용자 크루 탈퇴", description = "사용자가 특정 크루에서 탈퇴합니다.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "크루 탈퇴 성공",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Void.class))),
    @ApiResponse(
        responseCode = "401",
        description = "인증 오류",
        content =
            @Content(
                mediaType = "application/json",
                schema =
                    @Schema(
                        example =
                            "{\"code\": \"UNAUTHORIZED\", \"message\": \"Authorization 헤더가 필요합니다\"}"))),
    @ApiResponse(
        responseCode = "404",
        description = "크루를 찾을 수 없음",
        content =
            @Content(
                mediaType = "application/json",
                schema =
                    @Schema(example = "{\"code\": \"NOT_FOUND\", \"message\": \"크루를 찾을 수 없습니다\"}")))
  })
  ResponseEntity<Void> leaveCrew(
      @Parameter(description = "크루 ID", required = true) @PathVariable Long id);
}
