package com.crewcrew.domain.review.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.crewcrew.domain.crew.entity.Crew;
import com.crewcrew.domain.review.dto.ReviewResponse;

public interface ReviewCustomRepository {
  long getTotalRate(Crew crew);

  List<ReviewResponse.RatingsData> getRatingsData(Crew crew);

  long getReviewCount(Crew crew);

  Page<ReviewResponse.ReviewListInfo> findReviews(Long crewId, Pageable pageable);

  Slice<ReviewResponse.MemberReviewListResponse> getMemberReviews(Long MemberId, Pageable pageable);
}
