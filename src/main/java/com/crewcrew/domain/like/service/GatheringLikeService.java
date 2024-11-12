package com.crewcrew.domain.like.service;

import static com.crewcrew.global.common.exception.ErrorCode.GATHERING_NOT_FOUND;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crewcrew.domain.gathering.entity.Gathering;
import com.crewcrew.domain.gathering.repository.GatheringRepository;
import com.crewcrew.domain.like.entity.GatheringLike;
import com.crewcrew.domain.like.repository.GatheringLikeRepository;
import com.crewcrew.domain.member.dto.CustomUserDetails;
import com.crewcrew.domain.member.entity.Member;
import com.crewcrew.domain.member.repository.MemberRepository;
import com.crewcrew.global.common.exception.ApiException;
import com.crewcrew.global.common.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GatheringLikeService {
  private final GatheringLikeRepository gatheringLikeRepository;
  private final MemberRepository memberRepository;
  private final GatheringRepository gatheringRepository;

  @Transactional
  public void saveGatheringLike(Long gatheringId, CustomUserDetails userDetails) {
    Member member =
        memberRepository
            .findById(userDetails.getUserId())
            .orElseThrow(() -> new ApiException(ErrorCode.MEMBER_NOT_FOUND));

    Gathering gathering =
        gatheringRepository
            .findById(gatheringId)
            .orElseThrow(() -> new ApiException(GATHERING_NOT_FOUND));

    boolean exist =
        gatheringLikeRepository.existsByGatheringIdAndMemberId(gatheringId, member.getId());

    if (exist) {
      throw new ApiException(ErrorCode.DUPLICATE_LIKED);
    }

    GatheringLike gatheringLike =
        GatheringLike.builder().gathering(gathering).member(member).build();

    gatheringLikeRepository.save(gatheringLike);
  }

  @Transactional
  public void DeleteGatheringLike(Long gatheringId, CustomUserDetails userDetails) {
    Member member =
        memberRepository
            .findById(userDetails.getUserId())
            .orElseThrow(() -> new ApiException(ErrorCode.MEMBER_NOT_FOUND));

    Gathering gathering =
        gatheringRepository
            .findById(gatheringId)
            .orElseThrow(() -> new ApiException(GATHERING_NOT_FOUND));

    GatheringLike gatheringLike =
        gatheringLikeRepository
            .findByGatheringIdAndMemberId(gatheringId, member.getId())
            .orElseThrow(() -> new ApiException(ErrorCode.UNAUTHORIZED_DELETE_LIKED));

    gatheringLikeRepository.deleteById(gatheringLike.getId());
  }
}
