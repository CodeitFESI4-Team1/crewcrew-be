package com.crewcrew.domain.crew.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.crewcrew.domain.crew.dto.request.CrewCreateRequest;
import com.crewcrew.domain.crew.dto.request.CrewUpdateRequest;
import com.crewcrew.domain.crew.dto.response.CrewDetailResponse;
import com.crewcrew.domain.crew.service.*;
import com.crewcrew.domain.member.dto.CustomUserDetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/crews")
public class CrewController {

  private final CrewService crewService;

  @PostMapping
  public ResponseEntity<String> createCrew(
      @Validated @RequestBody CrewCreateRequest request,
      @AuthenticationPrincipal CustomUserDetails user) {

    crewService.createCrew(request, user.getUsername());

    return ResponseEntity.ok("크루가 생성 되었습니다.");
  }

  @GetMapping("/{crewId}")
  public ResponseEntity<CrewDetailResponse> getCrewDetail(@PathVariable Long crewId) {
    CrewDetailResponse response = crewService.getCrewDetail(crewId);
    return ResponseEntity.ok(response);
  }

  @PutMapping("/{crewId}")
  public ResponseEntity<String> updateCrew(
      @PathVariable Long crewId,
      @Validated @RequestBody CrewUpdateRequest request,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    crewService.updateCrew(crewId, request, userDetails.getUsername());
    return ResponseEntity.ok("크루가 성공적으로 수정되었습니다.");
  }

  @PostMapping("/{crewId}/join")
  public ResponseEntity<String> joinCrew(
      @PathVariable Long crewId, @AuthenticationPrincipal CustomUserDetails userDetails) {
    crewService.joinCrew(crewId, userDetails.getUsername());
    return ResponseEntity.ok("크루에 참여하였습니다.");
  }

  @DeleteMapping("/{crewId}")
  public ResponseEntity<Void> deleteCrew(
      @PathVariable Long crewId, @AuthenticationPrincipal CustomUserDetails userDetails) {
    crewService.deleteCrew(crewId, userDetails.getUsername());
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{crewId}/leave")
  public ResponseEntity<String> leaveCrew(
      @PathVariable Long crewId, @AuthenticationPrincipal CustomUserDetails userDetails) {
    crewService.leaveCrew(crewId, userDetails.getUsername());
    return ResponseEntity.ok("크루를 탈퇴 했습니다.");
  }
}
