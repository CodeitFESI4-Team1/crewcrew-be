package com.crewcrew.domain.crew.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crewcrew.domain.crew.dto.request.CrewCreateRequest;
import com.crewcrew.domain.crew.dto.response.CrewDetailResponse;
import com.crewcrew.domain.crew.entity.Crew;
import com.crewcrew.domain.crew.entity.MemberCrew;
import com.crewcrew.domain.crew.enums.MemberCrewStatus;
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

    MemberCrew memberCrew =
        MemberCrew.builder()
            .member(member)
            .crew(crew)
            .isCaptain(true)
            .status(MemberCrewStatus.JOINED)
            .build();

    memberCrewRepository.save(memberCrew);
  }

  private void validateCrewTitle(String title) {
    if (crewRepository.existsByTitleIgnoreCaseAndSpace(title)) {
      throw new IllegalArgumentException("이미 존재하는 크루 제목입니다.");
    }
  }

  public CrewDetailResponse getCrewDetail(Long crewId) {
    return crewRepository.findCrewDetailById(crewId).orElseThrow();
  }
}
