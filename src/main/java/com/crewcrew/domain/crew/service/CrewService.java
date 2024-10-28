package com.crewcrew.domain.crew.service;

import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crewcrew.domain.crew.dto.request.CrewCreateRequestDTO;
import com.crewcrew.domain.crew.dto.request.CrewFss;
import com.crewcrew.domain.crew.dto.response.*;
import com.crewcrew.domain.crew.entity.Crew;
import com.crewcrew.domain.crew.entity.CrewInfo;
import com.crewcrew.domain.crew.enums.ImageType;
import com.crewcrew.domain.crew.repository.CrewInfoRepository;
import com.crewcrew.domain.crew.repository.CrewRepository;
import com.crewcrew.domain.crew.repository.ImageRepository;
import com.crewcrew.domain.member.entity.Member;
import com.crewcrew.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrewService {

  private final CrewRepository crewRepository;
  private final MemberRepository memberRepository;
  private final ImageRepository imageRepository;
  private final CrewInfoRepository crewInfoRepository;

  @Transactional
  public CrewResponseDTO createCrew(CrewCreateRequestDTO request) {
    Member member = findMemberById(getMemberId());
    Crew savedCrew = saveCrew(request, member);
    List<ImageResponseDTO> images = getImagesByCrewId(savedCrew.getId());

    return convertToDTO(savedCrew, images);
  }

  @Transactional(readOnly = true)
  public Slice<CrewListResponseDTO> getCrew(CrewFss fss, Pageable pageable) {
    Slice<Crew> crewSlice = crewRepository.findFilteredCrews(fss, pageable);
    return crewSlice.map(
        e -> {
          List<ImageResponseDTO> images = getImagesByCrewId(e.getId());
          return convertToListDTO(e, images);
        });
  }

  @Transactional(readOnly = true)
  public Slice<CrewListResponseDTO> getCreatedCrew(Pageable pageable) {
    Member member = findMemberById(getMemberId());
    Slice<Crew> crews = crewRepository.findByMember(member, pageable);
    return crews.map(e -> convertToListDTO(e, getImagesByCrewId(e.getId())));
  }

  @Transactional(readOnly = true)
  public Slice<CrewListResponseDTO> getJoinedCrew(Pageable pageable) {
    List<Long> crewIds = crewInfoRepository.findCrewIdsByMemberId(getMemberId());

    if (crewIds.isEmpty()) {
      return new SliceImpl<>(Collections.emptyList(), pageable, false);
    }

    Slice<Crew> crewSlice = crewRepository.findAllById(crewIds, pageable);

    return crewSlice.map(
        crew -> {
          List<ImageResponseDTO> images = getImagesByCrewId(crew.getId());
          return convertToListDTO(crew, images);
        });
  }

  @Transactional(readOnly = true)
  public CrewDetailResponseDTO getCrewDetails(Long id) {
    Crew crew = findCrewById(id);
    List<JoinedParticipantDTO> participants = getParticipantsByCrewId(crew.getId());
    List<ImageResponseDTO> images = getImagesByCrewId(crew.getId());

    return convertToDetailDTO(crew, images, participants);
  }

  @Transactional(readOnly = true)
  public List<JoinedParticipantDTO> getParticipantsByCrewId(Long crewId) {
    List<CrewInfo> crewInfos = crewInfoRepository.findByCrewId(crewId);

    return crewInfos.stream()
        .map(
            crewInfo ->
                new JoinedParticipantDTO(
                    crewInfo.getMember().getId(), crewInfo.getMember().getName()))
        .toList();
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

  private CrewDetailResponseDTO convertToDetailDTO(
      Crew crew, List<ImageResponseDTO> images, List<JoinedParticipantDTO> participants) {
    return new CrewDetailResponseDTO(
        crew.getId(),
        crew.getType(),
        crew.getSubType(),
        crew.getName(),
        crew.getLocation(),
        crew.getDetailedLocation(),
        crew.getParticipantCount(),
        crew.getCapacity(),
        images,
        crew.getMember().getId(),
        crew.getCreatedAt(),
        crew.getUpdatedAt(),
        crew.getCanceledAt(),
        crew.getIsConfirmed(),
        participants);
  }

  private Crew findCrewById(Long id) {
    return crewRepository.findById(id).orElseThrow(() -> new RuntimeException("Crew not found"));
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
