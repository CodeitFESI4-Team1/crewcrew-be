package com.crewcrew.domain.crew.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.crewcrew.domain.crew.dto.request.CrewCreateRequestDTO;
import com.crewcrew.domain.crew.dto.response.CrewResponseDTO;
import com.crewcrew.domain.crew.service.CrewService;

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

    CrewResponseDTO response = crewService.createCrew(request);

    return ResponseEntity.ok(response);
  }
}
