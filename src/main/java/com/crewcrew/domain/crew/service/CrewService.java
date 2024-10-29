package com.crewcrew.domain.crew.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crewcrew.domain.crew.dto.request.*;
import com.crewcrew.domain.crew.dto.response.*;
import com.crewcrew.domain.crew.entity.*;
import com.crewcrew.domain.crew.enums.*;
import com.crewcrew.domain.crew.repository.*;
import com.crewcrew.domain.member.entity.Member;
import com.crewcrew.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CrewService {

  private final CrewRepository crewRepository;
  private final MemberRepository memberRepository;
  private final ImageRepository imageRepository;

  @Transactional
  public CrewResponseDTO createCrew(CrewCreateRequestDTO request) {
    Member member = findMemberById(getMemberId());
    Crew savedCrew = saveCrew(request, member);
    List<ImageResponseDTO> images = getImagesByCrewId(savedCrew.getId());

    return convertToDTO(savedCrew, images);
  }

  private Crew saveCrew(CrewCreateRequestDTO request, Member member) {
    Crew crew =
        Crew.builder()
            .location(request.location())
            .detailedLocation(request.detailedLocation())
            .type(request.type())
            .subType(request.subType())
            .name(request.name())
            .description(request.description())
            .capacity(request.capacity())
            .member(member)
            .build();
    return crewRepository.save(crew);
  }

  private List<ImageResponseDTO> getImagesByCrewId(Long crewId) {
    return imageRepository.findByReferenceIdAndImageType(crewId, ImageType.CREW).stream()
        .map(image -> new ImageResponseDTO(image.getImagePath()))
        .collect(Collectors.toList());
  }

  private CrewResponseDTO convertToDTO(Crew crew, List<ImageResponseDTO> images) {
    return new CrewResponseDTO(
        crew.getId(),
        crew.getType(),
        crew.getSubType(),
        crew.getName(),
        crew.getLocation(),
        crew.getDetailedLocation(),
        crew.getParticipantCount(),
        crew.getCapacity(),
        images,
        crew.getMember().getId());
  }

  private Member findMemberById(Long memberId) {
    return memberRepository
        .findById(memberId)
        .orElseThrow(() -> new RuntimeException("Member not found")); // 임시 에러 처리
  }

  // 임시 처리
  private Long getMemberId() {
    return 1L;
  }
}
