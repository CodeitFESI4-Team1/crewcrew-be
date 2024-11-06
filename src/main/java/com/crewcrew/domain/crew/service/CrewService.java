package com.crewcrew.domain.crew.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crewcrew.domain.crew.dto.request.CrewCreateRequest;
import com.crewcrew.domain.crew.dto.request.CrewUpdateRequest;
import com.crewcrew.domain.crew.dto.response.CrewDetailResponse;
import com.crewcrew.domain.crew.entity.Crew;
import com.crewcrew.domain.crew.entity.MemberCrew;
import com.crewcrew.domain.crew.enums.MainCategory;
import com.crewcrew.domain.crew.enums.SubCategory;
import com.crewcrew.domain.crew.repository.CrewRepository;
import com.crewcrew.domain.crew.repository.MemberCrewRepository;
import com.crewcrew.domain.member.entity.Member;
import com.crewcrew.domain.member.repository.MemberRepository;

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

  @Transactional
  public void createCrew(CrewCreateRequest request, String email) {
    validateCrewTitle(request.getTitle());
    Member member =
        memberRepository
            .findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("유저정보가 없습니다."));
    Crew crew = request.toEntity();
    crewRepository.save(crew);

    MemberCrew memberCrew = MemberCrew.builder().member(member).crew(crew).isCaptain(true).build();

    memberCrewRepository.save(memberCrew);
  }

  public CrewDetailResponse getCrewDetail(Long crewId) {
    return crewRepository.findCrewDetailById(crewId).orElseThrow();
  }

  @Transactional
  public void updateCrew(Long crewId, CrewUpdateRequest request, String email) {
    Crew crew = crewRepository.findById(crewId).orElseThrow();

    validateIsCaptain(crewId, email);
    validateTitle(crewId, request, crew);

    long currentMemberCount = memberCrewRepository.countByCrewId(crewId);
    validateTotalCount(currentMemberCount, request.getTotalCount());

    crew.update(
        request.getTitle(),
        MainCategory.fromValue(request.getMainCategory()),
        SubCategory.fromValue(request.getSubCategory()),
        request.getMainLocation(),
        request.getSubLocation(),
        request.getTotalCount(),
        request.getImageUrl());
  }

  private void validateTitle(Long crewId, CrewUpdateRequest request, Crew crew) {
    if (!crew.getTitle().equals(request.getTitle())
        && crewRepository.existsByTitleAndIdNot(request.getTitle(), crewId)) {
      throw new IllegalArgumentException("이미 존재하는 크루 제목입니다.");
    }
  }

  private void validateIsCaptain(Long crewId, String email) {
    boolean isCaptain =
        memberCrewRepository.existsByCrewIdAndMemberEmailAndIsCaptainIsTrue(crewId, email);
    if (!isCaptain) {
      throw new AccessDeniedException("크루장만 수정할 수 있습니다.");
    }
  }

  private void validateCrewTitle(String title) {
    if (crewRepository.existsByTitleIgnoreCaseAndSpace(title)) {
      throw new IllegalArgumentException("이미 존재하는 크루 제목입니다.");
    }
  }

  private void validateTotalCount(long currentMemberCount, int newTotalCount) {
    if (newTotalCount < currentMemberCount) {
      throw new IllegalArgumentException(
          String.format("총 인원은 현재 크루원 수(%d명) 이상이어야 합니다.", currentMemberCount));
    }
  }
}
