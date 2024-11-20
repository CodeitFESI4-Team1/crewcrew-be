package com.crewcrew.domain.crew.service;

import static com.crewcrew.global.common.exception.ErrorCode.*;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crewcrew.domain.crew.dto.request.CrewCreateRequest;
import com.crewcrew.domain.crew.dto.request.CrewSearchCondition;
import com.crewcrew.domain.crew.dto.request.CrewUpdateRequest;
import com.crewcrew.domain.crew.dto.response.CreateCrewResponse;
import com.crewcrew.domain.crew.dto.response.CrewDetailResponse;
import com.crewcrew.domain.crew.dto.response.CrewListResponse;
import com.crewcrew.domain.crew.dto.response.JoinedCrewResponse;
import com.crewcrew.domain.crew.entity.Crew;
import com.crewcrew.domain.crew.entity.MemberCrew;
import com.crewcrew.domain.crew.enums.MainCategory;
import com.crewcrew.domain.crew.enums.SubCategory;
import com.crewcrew.domain.crew.repository.CrewRepository;
import com.crewcrew.domain.crew.repository.MemberCrewRepository;
import com.crewcrew.domain.gathering.repository.GatheringParticipantRepository;
import com.crewcrew.domain.gathering.repository.GatheringRepository;
import com.crewcrew.domain.like.repository.GatheringLikeRepository;
import com.crewcrew.domain.member.entity.Member;
import com.crewcrew.domain.member.repository.MemberRepository;
import com.crewcrew.global.common.dto.PagedResponse;
import com.crewcrew.global.common.exception.ApiException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CrewService {

  private final CrewRepository crewRepository;
  private final MemberRepository memberRepository;
  private final MemberCrewRepository memberCrewRepository;
  private final GatheringLikeRepository gatheringLikeRepository;
  private final GatheringRepository gatheringRepository;
  private final GatheringParticipantRepository gatheringParticipantRepository;

  @Transactional
  public CreateCrewResponse createCrew(CrewCreateRequest request, String email) {
    validateCrewTitle(request.getTitle());
    Member member = findMember(email);
    Crew crew = request.toEntity();
    Crew savedCrew = crewRepository.save(crew);

    MemberCrew memberCrew =
        MemberCrew.builder().member(member).crew(savedCrew).isCaptain(true).build();

    memberCrewRepository.save(memberCrew);
    return CreateCrewResponse.of(savedCrew.getId());
  }

  public CrewDetailResponse getCrewDetail(Long crewId) {
    return crewRepository
        .findCrewDetailById(crewId)
        .orElseThrow(() -> new ApiException(CREW_NOT_FOUND));
  }

  @Transactional
  public void updateCrew(Long crewId, CrewUpdateRequest request, String email) {
    Crew crew = findCrew(crewId);
    validateIsCaptain(crewId, email);
    validateTitle(crewId, request, crew);

    long currentMemberCount = memberCrewRepository.countByCrewId(crewId);
    validateTotalCount(currentMemberCount, request.getTotalCount());

    crew.update(
        request.getTitle(),
        request.getIntroduce(),
        MainCategory.fromLabel(request.getMainCategory()),
        SubCategory.fromLabel(request.getSubCategory()),
        request.getMainLocation(),
        request.getSubLocation() == null ? "" : request.getSubLocation(),
        request.getTotalCount(),
        request.getImageUrl());
  }

  @Transactional
  public void joinCrew(Long crewId, String email) {
    Crew crew = findCrew(crewId);
    Member member = findMember(email);

    if (memberCrewRepository.existsByCrewIdAndMemberId(crewId, member.getId())) {
      throw new ApiException(ALREADY_CREW_MEMBER);
    }

    long currentMemberCount = memberCrewRepository.countByCrewId(crewId);
    if (currentMemberCount >= crew.getTotalCount()) {
      throw new ApiException(CREW_CAPACITY_EXCEEDED);
    }

    MemberCrew memberCrew = MemberCrew.builder().member(member).crew(crew).isCaptain(false).build();

    memberCrewRepository.save(memberCrew);
  }

  @Transactional
  public void deleteCrew(Long crewId, String email) {
    Crew crew = findCrew(crewId);
    Member member = findMember(email);
    MemberCrew memberCrew = findMemberCrew(crewId, member);

    if (!memberCrew.isCaptain()) {
      throw new ApiException(CAPTAIN_PERMISSION_DENIED);
    }
    gatheringLikeRepository.deleteByGatheringCrewId(crewId);
    gatheringParticipantRepository.deleteByGatheringCrewId(crewId);
    memberCrewRepository.deleteByCrewId(crewId);
    gatheringRepository.deleteByCrewId(crewId);
    crewRepository.delete(crew);
  }

  @Transactional
  public void leaveCrew(Long crewId, String email) {
    Member member = findMember(email);
    MemberCrew memberCrew = findMemberCrew(crewId, member);

    if (memberCrew.isCaptain()) {
      throw new ApiException(CAPTAIN_LEAVE_DENIED);
    }

    gatheringParticipantRepository.deleteByCrewIdAndMemberId(crewId, member.getId());
    memberCrewRepository.delete(memberCrew);
  }

  public PagedResponse<JoinedCrewResponse> getJoinedCrews(String email, Pageable pageable) {
    Slice<JoinedCrewResponse> slice = crewRepository.findJoinedCrews(email, pageable);
    return new PagedResponse<>(slice.getContent(), slice.hasNext());
  }

  public PagedResponse<JoinedCrewResponse> getHostedCrews(String email, Pageable pageable) {
    Slice<JoinedCrewResponse> slice = crewRepository.findCrewsByHost(email, pageable);
    return new PagedResponse<>(slice.getContent(), slice.hasNext());
  }

  public PagedResponse<CrewListResponse> searchCrews(
      CrewSearchCondition condition, Pageable pageable) {
    Slice<CrewListResponse> slice = crewRepository.searchCrews(condition, pageable);

    List<CrewListResponse> convertedContent =
        slice.getContent().stream().map(this::convertCategoryToLabel).toList();

    return new PagedResponse<>(convertedContent, slice.hasNext());
  }

  private void validateTitle(Long crewId, CrewUpdateRequest request, Crew crew) {
    if (!crew.getTitle().equals(request.getTitle())
        && crewRepository.existsByTitleAndIdNot(request.getTitle(), crewId)) {
      throw new ApiException(DUPLICATE_CREW_TITLE);
    }
  }

  private void validateIsCaptain(Long crewId, String email) {
    boolean isCaptain =
        memberCrewRepository.existsByCrewIdAndMemberEmailAndIsCaptainIsTrue(crewId, email);
    if (!isCaptain) {
      throw new ApiException(CAPTAIN_PERMISSION_DENIED);
    }
  }

  private void validateCrewTitle(String title) {
    if (crewRepository.existsByTitleIgnoreCaseAndSpace(title)) {
      throw new ApiException(DUPLICATE_CREW_TITLE);
    }
  }

  private void validateTotalCount(long currentMemberCount, int newTotalCount) {
    if (newTotalCount < currentMemberCount) {
      throw new ApiException(INVALID_TOTAL_COUNT);
    }
  }

  private Crew findCrew(Long crewId) {
    return crewRepository.findById(crewId).orElseThrow(() -> new ApiException(CREW_NOT_FOUND));
  }

  private Member findMember(String email) {
    return memberRepository
        .findByEmail(email)
        .orElseThrow(() -> new ApiException(MEMBER_NOT_FOUND));
  }

  private MemberCrew findMemberCrew(Long crewId, Member member) {
    return memberCrewRepository
        .findByCrewIdAndMemberId(crewId, member.getId())
        .orElseThrow(() -> new ApiException(CREW_MEMBER_NOT_FOUND));
  }

  private CrewListResponse convertCategoryToLabel(CrewListResponse crew) {
    return CrewListResponse.builder()
        .id(crew.getId())
        .mainCategory(MainCategory.valueOf(crew.getMainCategory()).getLabel())
        .subCategory(SubCategory.valueOf(crew.getSubCategory()).getLabel())
        .title(crew.getTitle())
        .mainLocation(crew.getMainLocation())
        .subLocation(crew.getSubLocation())
        .participantCount(crew.getParticipantCount())
        .totalCount(crew.getTotalCount())
        .imageUrl(crew.getImageUrl())
        .totalGatheringCount(crew.getTotalGatheringCount())
        .build();
  }
}
