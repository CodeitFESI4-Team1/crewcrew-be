package com.crewcrew.domain.gathering.service;

import static com.crewcrew.global.common.exception.ErrorCode.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crewcrew.domain.crew.entity.Crew;
import com.crewcrew.domain.crew.entity.MemberCrew;
import com.crewcrew.domain.crew.repository.CrewRepository;
import com.crewcrew.domain.crew.repository.MemberCrewRepository;
import com.crewcrew.domain.gathering.dto.request.GatheringCreateRequest;
import com.crewcrew.domain.gathering.dto.response.*;
import com.crewcrew.domain.gathering.entity.Gathering;
import com.crewcrew.domain.gathering.entity.GatheringParticipant;
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
public class GatheringService {

  private final GatheringRepository gatheringRepository;
  private final GatheringParticipantRepository participantRepository;
  private final GatheringLikeRepository likeRepository;
  private final CrewRepository crewRepository;
  private final MemberRepository memberRepository;
  private final MemberCrewRepository memberCrewRepository;

  @Transactional
  public void createGathering(Long crewId, GatheringCreateRequest request, String email) {
    Crew crew = crewRepository.findById(crewId).orElseThrow(() -> new ApiException(CREW_NOT_FOUND));

    Member member =
        memberRepository.findByEmail(email).orElseThrow(() -> new ApiException(MEMBER_NOT_FOUND));

    memberCrewRepository
        .findByCrewIdAndMemberId(crewId, member.getId())
        .orElseThrow(() -> new ApiException(CREW_MEMBER_NOT_FOUND));

    validateDateTime(request.getDateTime());
    validateDuplicateGathering(crewId, request.getDateTime());

    Gathering gathering = request.toEntity(crew);
    Gathering savedGathering = gatheringRepository.save(gathering);

    GatheringParticipant captainParticipant =
        GatheringParticipant.builder()
            .gathering(savedGathering)
            .member(member)
            .isGatheringCaptain(true)
            .build();

    participantRepository.save(captainParticipant);
  }

  public GatheringDetailResponse getGatheringDetail(Long crewId, Long gatheringId, String email) {
    Gathering gathering =
        gatheringRepository
            .findByIdAndCrewId(gatheringId, crewId)
            .orElseThrow(() -> new ApiException(GATHERING_NOT_FOUND));

    List<GatheringParticipant> participants = participantRepository.findByGatheringId(gatheringId);
    List<ParticipantResponse> participantResponses =
        participants.stream().map(p -> ParticipantResponse.from(p.getMember())).toList();

    if (email == null) {
      return GatheringDetailResponse.from(gathering, participantResponses);
    }

    Member currentMember =
        memberRepository.findByEmail(email).orElseThrow(() -> new ApiException(MEMBER_NOT_FOUND));

    memberCrewRepository
        .findByCrewIdAndMemberId(crewId, currentMember.getId())
        .orElseThrow(() -> new ApiException(CREW_MEMBER_NOT_FOUND));

    boolean isLiked =
        likeRepository.existsByGatheringIdAndMemberId(gatheringId, currentMember.getId());
    boolean isGatheringCaptain =
        participants.stream()
            .anyMatch(
                p -> p.getMember().getId().equals(currentMember.getId()) && p.isGatheringCaptain());
    boolean isParticipant =
        participants.stream().anyMatch(p -> p.getMember().getId().equals(currentMember.getId()));

    return GatheringDetailResponse.of(
        gathering, participantResponses, isLiked, isGatheringCaptain, isParticipant);
  }

  @Transactional
  public void joinGathering(Long crewId, Long gatheringId, String email) {
    Member member =
        memberRepository.findByEmail(email).orElseThrow(() -> new ApiException(MEMBER_NOT_FOUND));

    memberCrewRepository
        .findByCrewIdAndMemberId(crewId, member.getId())
        .orElseThrow(() -> new ApiException(CREW_MEMBER_NOT_FOUND));

    Gathering gathering =
        gatheringRepository
            .findByIdAndCrewId(gatheringId, crewId)
            .orElseThrow(() -> new ApiException(GATHERING_NOT_FOUND));

    validateGatheringJoin(gathering, gatheringId, member.getId());

    GatheringParticipant participant =
        GatheringParticipant.builder()
            .gathering(gathering)
            .member(member)
            .isGatheringCaptain(false)
            .build();

    participantRepository.save(participant);
  }

  public List<GatheringListResponse> getGatheringList(Long crewId, String email) {
    LocalDateTime now = LocalDateTime.now();

    if (email == null) {
      return gatheringRepository.findAllByCrewId(crewId, now);
    }

    Member member =
        memberRepository.findByEmail(email).orElseThrow(() -> new ApiException(MEMBER_NOT_FOUND));

    return gatheringRepository.findAllByCrewIdWithLikeInfo(crewId, member.getId(), now);
  }

  public List<MyGatheringListResponse> getMyHostedGatherings(
      String email, LocalDateTime startDateTime) {
    Member member =
        memberRepository.findByEmail(email).orElseThrow(() -> new ApiException(MEMBER_NOT_FOUND));

    return gatheringRepository.findAllByMemberAndIsCaptain(member.getId(), startDateTime);
  }

  public List<MyGatheringListResponse> getMyParticipatedGatherings(
      String email, LocalDateTime startDateTime) {
    Member member =
        memberRepository.findByEmail(email).orElseThrow(() -> new ApiException(MEMBER_NOT_FOUND));

    return gatheringRepository.findAllParticipatedGathering(member.getId(), startDateTime);
  }

  public PagedResponse<GatheringReviewResponse> getReviewableGatherings(
      String email, Pageable pageable) {

    Member member =
        memberRepository.findByEmail(email).orElseThrow(() -> new ApiException(MEMBER_NOT_FOUND));

    Slice<GatheringReviewResponse> gatherings =
        gatheringRepository.findAllReviewableGatherings(
            member.getId(), LocalDateTime.now(), pageable);

    if (gatherings.isEmpty()) {
      return new PagedResponse<>(Collections.emptyList(), false);
    }

    List<Long> gatheringIds =
        gatherings.getContent().stream().map(GatheringReviewResponse::getId).toList();

    Map<Long, List<GatheringParticipantResponse>> participantsByGathering =
        gatheringRepository.findParticipantsByGatheringIds(gatheringIds).stream()
            .collect(
                Collectors.groupingBy(
                    GatheringParticipantMapping::getGatheringId,
                    Collectors.mapping(
                        GatheringParticipantMapping::getParticipant, Collectors.toList())));

    List<GatheringReviewResponse> limitedParticipants =
        gatherings.getContent().stream()
            .map(
                gathering -> {
                  List<GatheringParticipantResponse> participants =
                      participantsByGathering
                          .getOrDefault(gathering.getId(), Collections.emptyList())
                          .stream()
                          .limit(4)
                          .toList();

                  return GatheringReviewResponse.builder()
                      .id(gathering.getId())
                      .title(gathering.getTitle())
                      .dateTime(gathering.getDateTime())
                      .location(gathering.getLocation())
                      .currentCount(gathering.getCurrentCount())
                      .totalCount(gathering.getTotalCount())
                      .imageUrl(gathering.getImageUrl())
                      .participants(participants)
                      .build();
                })
            .toList();

    return new PagedResponse<>(limitedParticipants, gatherings.hasNext());
  }

  @Transactional
  public void leaveGatheringParticipation(String email, Long crewId, Long gatheringId) {
    Member member =
        memberRepository.findByEmail(email).orElseThrow(() -> new ApiException(MEMBER_NOT_FOUND));

    Crew crew = crewRepository.findById(crewId).orElseThrow(() -> new ApiException(CREW_NOT_FOUND));

    Gathering gathering =
        gatheringRepository
            .findById(gatheringId)
            .orElseThrow(() -> new ApiException(GATHERING_NOT_FOUND));

    MemberCrew memberCrew =
        memberCrewRepository
            .findByMemberAndCrew(member, gathering.getCrew())
            .orElseThrow(() -> new ApiException(CREW_MEMBER_NOT_FOUND));

    GatheringParticipant participant =
        participantRepository
            .findByGatheringAndMember(gathering, member)
            .orElseThrow(() -> new ApiException(NOT_GATHERING_PARTICIPANT));

    if (participant.isGatheringCaptain()) {
      throw new ApiException(GATHERING_CAPTAIN_LEAVE_DENIED);
    }

    if (gathering.getDateTime().isBefore(LocalDateTime.now())) {
      throw new ApiException(GATHERING_COMPLETED);
    }

    participantRepository.delete(participant);
  }

  @Transactional
  public void deleteGathering(String email, Long crewId, Long gatheringId) {
    Member member =
        memberRepository.findByEmail(email).orElseThrow(() -> new ApiException(MEMBER_NOT_FOUND));

    Crew crew = crewRepository.findById(crewId).orElseThrow(() -> new ApiException(CREW_NOT_FOUND));

    Gathering gathering =
        gatheringRepository
            .findById(gatheringId)
            .orElseThrow(() -> new ApiException(GATHERING_NOT_FOUND));

    if (!gathering.getCrew().getId().equals(crewId)) {
      throw new ApiException(GATHERING_NOT_IN_CREW);
    }

    if (gathering.getDateTime().isBefore(LocalDateTime.now())) {
      throw new ApiException(GATHERING_COMPLETED);
    }

    GatheringParticipant captain =
        participantRepository
            .findByGatheringAndMember(gathering, member)
            .orElseThrow(() -> new ApiException(NOT_GATHERING_PARTICIPANT));

    if (!captain.isGatheringCaptain()) {
      throw new ApiException(GATHERING_CAPTAIN_PERMISSION_DENIED);
    }

    likeRepository.deleteByGathering(gathering);
    participantRepository.deleteByGathering(gathering);

    gatheringRepository.delete(gathering);
  }

  private void validateGatheringJoin(Gathering gathering, Long gatheringId, Long memberId) {
    if (participantRepository.existsByGatheringIdAndMemberId(gatheringId, memberId)) {
      throw new ApiException(ALREADY_GATHERING_PARTICIPANT);
    }

    long currentParticipants = participantRepository.countByGatheringId(gatheringId);
    if (currentParticipants >= gathering.getTotalCount()) {
      throw new ApiException(GATHERING_CAPACITY_EXCEEDED);
    }

    if (gathering.getDateTime().isBefore(LocalDateTime.now())) {
      throw new ApiException(PAST_GATHERING_JOIN_DENIED);
    }
  }

  private void validateDateTime(LocalDateTime dateTime) {
    try {
      if (dateTime.isBefore(LocalDateTime.now())) {
        throw new ApiException(INVALID_GATHERING_DATETIME);
      }
    } catch (DateTimeParseException e) {
      throw new ApiException(INVALID_DATETIME_FORMAT);
    }
  }

  private void validateDuplicateGathering(Long crewId, LocalDateTime dateTime) {
    if (gatheringRepository.existsByCrewIdAndDateTime(crewId, dateTime)) {
      throw new ApiException(DUPLICATE_GATHERING_DATETIME);
    }
  }
}
