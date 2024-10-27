package com.crewcrew.domain.crew.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.crewcrew.domain.crew.dto.request.*;
import com.crewcrew.domain.crew.dto.response.*;
import com.crewcrew.domain.crew.service.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/crews")
public class CrewController {
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
}
