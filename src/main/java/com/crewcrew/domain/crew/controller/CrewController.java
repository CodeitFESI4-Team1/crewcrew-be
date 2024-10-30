package com.crewcrew.domain.crew.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.crewcrew.domain.crew.controller.docs.CrewControllerDocs;
import com.crewcrew.domain.crew.dto.request.*;
import com.crewcrew.domain.crew.dto.response.*;
import com.crewcrew.domain.crew.service.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/crews")
public class CrewController implements CrewControllerDocs {
  private final CrewService crewService;

  @PostMapping
  public ResponseEntity<CrewResponseDTO> createCrew(@RequestBody CrewCreateRequestDTO request) {
    return new ResponseEntity<>(crewService.createCrew(request), HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<Slice<CrewListResponseDTO>> getCrew(
      CrewFss fss,
      @PageableDefault(size = 10, page = 0, sort = "updatedAt", direction = Sort.Direction.DESC)
          Pageable pageable) {
    return ResponseEntity.ok(crewService.getCrew(fss, pageable));
  }

  @GetMapping("/created")
  public ResponseEntity<Slice<CrewListResponseDTO>> getCreatedCrew(
      @PageableDefault(size = 5, page = 0, sort = "updatedAt", direction = Sort.Direction.DESC)
          Pageable pageable) {
    Slice<CrewListResponseDTO> crews = crewService.getCreatedCrew(pageable);
    return ResponseEntity.ok(crews);
  }

  @GetMapping("/joined")
  public ResponseEntity<Slice<CrewListResponseDTO>> getJoinedCrew(
      @PageableDefault(size = 5, page = 0, sort = "updatedAt", direction = Sort.Direction.DESC)
          Pageable pageable) {
    Slice<CrewListResponseDTO> crews = crewService.getJoinedCrew(pageable);
    return ResponseEntity.ok(crews);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CrewDetailResponseDTO> getCrewDetails(@PathVariable Long id) {
    CrewDetailResponseDTO crewDetails = crewService.getCrewDetails(id);
    return ResponseEntity.ok(crewDetails);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<CrewResponseDTO> updateCrew(
      @PathVariable Long id, @RequestBody CrewUpdateRequestDTO request) {
    CrewResponseDTO updatedCrew = crewService.updateCrew(id, request);
    return ResponseEntity.ok(updatedCrew);
  }

  @PostMapping("/{id}/join")
  public ResponseEntity<Void> joinCrew(@PathVariable Long id) {
    crewService.join(id);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PatchMapping("/{id}/cancel")
  public ResponseEntity<CrewResponseDTO> cancelCrew(@PathVariable Long id) {
    CrewResponseDTO canceledCrew = crewService.cancelCrew(id);
    return ResponseEntity.ok(canceledCrew);
  }

  @DeleteMapping("/{id}/leave")
  public ResponseEntity<Void> leaveCrew(@PathVariable Long id) {
    crewService.leaveCrew(id);
    return ResponseEntity.ok().build();
  }
}
