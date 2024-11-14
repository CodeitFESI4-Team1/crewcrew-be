package com.crewcrew.domain.like.repository;

import java.util.List;

import org.springframework.data.domain.*;

import com.crewcrew.domain.like.dto.GatheringLikeResponse;
import com.crewcrew.domain.like.entity.QGatheringLike;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class GatheringLikeRepositoryImpl implements GatheringLikeCustomRepository {
  private final JPAQueryFactory queryFactory;

  @Override
  public Page<GatheringLikeResponse.GatheringLikeList> getMemberLikes(
      Long userId, Pageable pageable) {
    JPAQuery<GatheringLikeResponse.GatheringLikeList> query =
        queryFactory
            .select(
                Projections.fields(
                    GatheringLikeResponse.GatheringLikeList.class,
                    QGatheringLike.gatheringLike.gathering.id.as("id"),
                    QGatheringLike.gatheringLike.gathering.title.as("title"),
                    QGatheringLike.gatheringLike.gathering.dateTime.as("dateTime"),
                    QGatheringLike.gatheringLike.gathering.location.as("location"),
                    QGatheringLike.gatheringLike.gathering.count().as("currentCount"),
                    QGatheringLike.gatheringLike.gathering.totalCount.as("totalCount"),
                    QGatheringLike.gatheringLike.gathering.imageUrl.as("imageUrl")))
            .from(QGatheringLike.gatheringLike)
            .where(QGatheringLike.gatheringLike.member.id.eq(userId))
            .groupBy(QGatheringLike.gatheringLike.gathering.id)
            .orderBy(QGatheringLike.gatheringLike.gathering.dateTime.asc());

    List<GatheringLikeResponse.GatheringLikeList> content =
        query.offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();

    Long count =
        queryFactory
            .select(QGatheringLike.gatheringLike.count())
            .from(QGatheringLike.gatheringLike)
            .where(QGatheringLike.gatheringLike.member.id.eq(userId))
            .fetchOne();

    long total = count != null ? count : 0L;

    return new PageImpl<>(content, pageable, total);
  }
}
