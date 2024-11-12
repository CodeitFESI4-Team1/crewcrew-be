package com.crewcrew.domain.like.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import com.crewcrew.domain.like.dto.GatheringLikeResponse;
import com.crewcrew.domain.like.entity.QGatheringLike;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class GatheringLikeRepositoryImpl implements GatheringLikeCustomRepository {
  private final JPAQueryFactory queryFactory;

  @Override
  public Slice<GatheringLikeResponse.GatheringLikeList> getMemberLikes(
      Long userId, Pageable pageable) {
    List<GatheringLikeResponse.GatheringLikeList> gatheringLikes =
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
            .orderBy(QGatheringLike.gatheringLike.gathering.dateTime.asc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize() + 1)
            .fetch();

    boolean hasNext = false;
    if (gatheringLikes.size() > pageable.getPageSize()) {
      gatheringLikes.remove(gatheringLikes.size() - 1);
      hasNext = true;
    }

    return new SliceImpl<>(gatheringLikes, pageable, hasNext);
  }
}
