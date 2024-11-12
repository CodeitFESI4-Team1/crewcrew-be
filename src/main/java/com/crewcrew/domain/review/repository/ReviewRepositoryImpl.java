package com.crewcrew.domain.review.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import com.crewcrew.domain.crew.entity.Crew;
import com.crewcrew.domain.crew.entity.QCrew;
import com.crewcrew.domain.review.dto.ReviewResponse;
import com.crewcrew.domain.review.entity.QReview;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewCustomRepository {
  private final JPAQueryFactory queryFactory;

  @Override
  public long getReviewCount(Crew crew) {
    Long count =
        queryFactory
            .select(QReview.review.count())
            .from(QReview.review)
            .where(QReview.review.crew.id.eq(crew.getId()))
            .fetchOne();
    return Optional.ofNullable(count).orElse(0L);
  }

  @Override
  public Slice<ReviewResponse.ReviewListInfo> findReviews(Long crewId, Pageable pageable) {
    List<ReviewResponse.ReviewListInfo> crews =
        queryFactory
            .select(
                Projections.constructor(
                    ReviewResponse.ReviewListInfo.class,
                    QCrew.crew.id,
                    QReview.review.id,
                    QReview.review.rate,
                    QReview.review.comment,
                    QReview.review.createdAt,
                    Projections.constructor(
                        ReviewResponse.ReviewerType.class,
                        QReview.review.member.id,
                        QReview.review.member.nickName,
                        QReview.review.member.profileImageUrl)))
            .from(QReview.review)
            .where(QReview.review.crew.id.eq(crewId))
            .orderBy(QReview.review.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize() + 1)
            .fetch();

    boolean hasNext = false;
    if (crews.size() > pageable.getPageSize()) {
      crews.remove(crews.size() - 1);
      hasNext = true;
    }

    return new SliceImpl<>(crews, pageable, hasNext);
  }

  @Override
  public long getTotalRate(Crew crew) {
    return Optional.ofNullable(
            queryFactory
                .select(QReview.review.rate.sum())
                .from(QReview.review)
                .where(QReview.review.crew.id.eq(crew.getId()))
                .fetchFirst())
        .orElse(0L);
  }

  @Override
  public List<ReviewResponse.RatingsData> getRatingsData(Crew crew) {
    QReview review = QReview.review;

    List<ReviewResponse.RatingsData> ratingsData =
        queryFactory
            .select(
                Projections.constructor(
                    ReviewResponse.RatingsData.class, review.rate, review.count()))
            .from(review)
            .where(review.crew.id.eq(crew.getId()))
            .groupBy(review.rate)
            .orderBy(review.rate.desc())
            .fetch();

    return ratingsData;
  }
}
