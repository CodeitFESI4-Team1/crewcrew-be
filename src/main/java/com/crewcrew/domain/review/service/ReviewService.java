package com.crewcrew.domain.review.service;

import static com.crewcrew.global.common.exception.ErrorCode.CREW_NOT_FOUND;
import static com.crewcrew.global.common.exception.ErrorCode.GATHERING_NOT_FOUND;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crewcrew.domain.crew.entity.Crew;
import com.crewcrew.domain.crew.repository.CrewRepository;
import com.crewcrew.domain.gathering.entity.Gathering;
import com.crewcrew.domain.gathering.repository.GatheringRepository;
import com.crewcrew.domain.member.dto.CustomUserDetails;
import com.crewcrew.domain.member.entity.Member;
import com.crewcrew.domain.member.repository.MemberRepository;
import com.crewcrew.domain.review.dto.ReviewRequest;
import com.crewcrew.domain.review.dto.ReviewResponse;
import com.crewcrew.domain.review.entity.Review;
import com.crewcrew.domain.review.repository.ReviewRepository;
import com.crewcrew.global.common.dto.PagedResponse;
import com.crewcrew.global.common.exception.ApiException;
import com.crewcrew.global.common.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {
  private final ReviewRepository reviewRepository;
  private final MemberRepository memberRepository;
  private final CrewRepository crewRepository;
  private final GatheringRepository gatheringRepository;

  public ReviewResponse.ReviewRateInfo getReviewRateInfo(Long crewId) {
    Crew crew = crewRepository.findById(crewId).orElseThrow(() -> new ApiException(CREW_NOT_FOUND));

    long totalRate = reviewRepository.getTotalRate(crew);
    long reviewCount = reviewRepository.getReviewCount(crew);
    double averageRate = reviewCount > 0 ? (double) totalRate / reviewCount : 0.0;
    List<ReviewResponse.RatingsData> ratingsData = reviewRepository.getRatingsData(crew);

    return ReviewResponse.ReviewRateInfo.builder()
        .totalRate(reviewCount)
        .averageRate(averageRate)
        .ratingsData(ratingsData)
        .build();
  }

  public PagedResponse<ReviewResponse.ReviewListInfo> getPagedReviews(
      Long crewId, Pageable pageable) {
    Slice<ReviewResponse.ReviewListInfo> slice = reviewRepository.findReviews(crewId, pageable);
    return new PagedResponse<>(slice.getContent(), slice.hasNext());
  }

  @Transactional
  public void makeReview(
      Long gatheringId, CustomUserDetails userDetails, ReviewRequest.ReviewType reviewType) {
    Member member =
        memberRepository
            .findById(userDetails.getUserId())
            .orElseThrow(() -> new ApiException(ErrorCode.MEMBER_NOT_FOUND));

    Gathering gathering =
        gatheringRepository
            .findById(gatheringId)
            .orElseThrow(() -> new ApiException(GATHERING_NOT_FOUND));

    boolean exist = reviewRepository.existsByMemberIdAndGatheringId(member.getId(), gatheringId);
    if (exist) {
      throw new ApiException(ErrorCode.DUPLICATE_REVIEW);
    }

    Review review =
        Review.builder()
            .crew(gathering.getCrew())
            .gathering(gathering)
            .member(member)
            .rate(reviewType.getRate())
            .comment(reviewType.getComment())
            .build();

    reviewRepository.save(review);
  }

  @Transactional
  public void deleteReview(Long reviewId, CustomUserDetails userDetails) {
    boolean exist = reviewRepository.existsByIdAndMemberId(reviewId, userDetails.getUserId());
    if (!exist) {
      throw new ApiException(ErrorCode.UNAUTHORIZED_DELETE_REVIEW);
    }

    reviewRepository.deleteById(reviewId);
  }

  public PagedResponse<ReviewResponse.MemberReviewListResponse> getMemberReviews(
      Pageable pageable, CustomUserDetails userDetails) {
    Slice<ReviewResponse.MemberReviewListResponse> slice =
        reviewRepository.getMemberReviews(userDetails.getUserId(), pageable);

    return new PagedResponse<>(slice.getContent(), slice.hasNext());
  }
}
