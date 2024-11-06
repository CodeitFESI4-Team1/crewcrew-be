package com.crewcrew.domain.crew.repository;

import java.util.List;
import java.util.Optional;

import com.crewcrew.domain.crew.dto.response.CrewDetailResponse;
import com.crewcrew.domain.crew.entity.Crew;
import com.crewcrew.domain.crew.entity.QCrew;
import com.crewcrew.domain.crew.entity.QMemberCrew;
import com.crewcrew.domain.gathering.QGathering;
import com.crewcrew.domain.member.entity.QMember;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CrewRepositoryImpl implements CrewCustomRepository {
  private final JPAQueryFactory queryFactory;

  @Override
  public boolean existsByTitleIgnoreCaseAndSpace(String title) {
    return queryFactory
            .selectOne()
            .from(QCrew.crew)
            .where(QCrew.crew.title.trim().toLowerCase().eq(title.trim().toLowerCase()))
            .fetchFirst()
        != null;
  }

  @Override
  public Optional<CrewDetailResponse> findCrewDetailById(Long crewId) {
    Crew crew = queryFactory.selectFrom(QCrew.crew).where(QCrew.crew.id.eq(crewId)).fetchOne();

    if (crew == null) {
      return Optional.empty();
    }

    List<CrewDetailResponse.CrewMemberResponse> members =
        queryFactory
            .select(
                Projections.constructor(
                    CrewDetailResponse.CrewMemberResponse.class,
                    QMember.member.id,
                    QMember.member.nickName,
                    QMember.member.profileImageUrl,
                    QMemberCrew.memberCrew.isCaptain))
            .from(QMemberCrew.memberCrew)
            .join(QMemberCrew.memberCrew.member, QMember.member)
            .where(QMemberCrew.memberCrew.crew.id.eq(crewId))
            .fetch();

    Long gatheringCount =
        queryFactory
            .select(QGathering.gathering.count())
            .from(QGathering.gathering)
            .where(QGathering.gathering.crew.id.eq(crewId))
            .fetchOne();

    return Optional.of(
        CrewDetailResponse.builder()
            .id(crew.getId())
            .title(crew.getTitle())
            .mainLocation(crew.getMainLocation())
            .subLocation(crew.getSubLocation())
            .participantCount(members.size())
            .totalCount(crew.getTotalCount())
            .imageUrl(crew.getImageUrl())
            .isConfirmed(crew.isConfirmed())
            .totalGatheringCount(gatheringCount != null ? gatheringCount.intValue() : 0)
            .crewMembers(members)
            .build());
  }
}
