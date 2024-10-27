package com.crewcrew.domain.crew.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crewcrew.domain.crew.dto.request.CrewCreateRequestDTO;
import com.crewcrew.domain.crew.dto.request.CrewFss;
import com.crewcrew.domain.crew.dto.response.CrewListResponseDTO;
import com.crewcrew.domain.crew.dto.response.CrewResponseDTO;
import com.crewcrew.domain.crew.dto.response.ImageResponseDTO;
import com.crewcrew.domain.crew.entity.Crew;
import com.crewcrew.domain.crew.repository.CrewRepository;
import com.crewcrew.domain.crew.repository.ImageRepository;
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

  @Transactional(readOnly = true)
  public Slice<CrewListResponseDTO> getCrew(CrewFss fss, Pageable pageable) {
    Slice<Crew> entities = crewRepository.findFilteredCrews(fss, pageable);
    return entities.map(
        e -> {
          List<ImageResponseDTO> images = getImagesByCrewId(e.getId());
          return convertToListDTO(e, images);
        });
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
    return imageRepository.findByReferenceId(crewId).stream()
        .map(image -> new ImageResponseDTO(image.getImagePath()))
        .toList();
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

  private CrewListResponseDTO convertToListDTO(Crew crew, List<ImageResponseDTO> images) {
    return new CrewListResponseDTO(
        crew.getId(),
        crew.getType(),
        crew.getSubType(),
        crew.getName(),
        crew.getDescription(),
        crew.getLocation(),
        crew.getDetailedLocation(),
        crew.getParticipantCount(),
        crew.getCapacity(),
        images,
        crew.getMember().getId(),
        crew.getCreatedAt(),
        crew.getUpdatedAt(),
        crew.getCanceledAt(),
        crew.getIsConfirmed());
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
