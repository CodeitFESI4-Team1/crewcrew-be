package com.crewcrew.domain.crew.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import com.crewcrew.domain.crew.dto.request.CrewSearchCondition;
import com.crewcrew.domain.crew.dto.response.CrewDetailResponse;
import com.crewcrew.domain.crew.dto.response.CrewListResponse;
import com.crewcrew.domain.crew.dto.response.JoinedCrewResponse;
import com.crewcrew.domain.crew.entity.Crew;
import com.crewcrew.domain.crew.entity.QCrew;
import com.crewcrew.domain.crew.entity.QMemberCrew;
import com.crewcrew.domain.crew.enums.MainCategory;
import com.crewcrew.domain.crew.enums.SubCategory;
import com.crewcrew.domain.gathering.entity.QGathering;
import com.crewcrew.domain.member.entity.QMember;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
                    QMember.member.email,
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
            .introduce(crew.getIntroduce())
            .mainCategory(crew.getMainCategory().getLabel())
            .subCategory(crew.getSubCategory().getLabel())
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

  @Override
  public Slice<JoinedCrewResponse> findJoinedCrews(String email, Pageable pageable) {
    List<JoinedCrewResponse> crews =
        queryFactory
            .select(
                Projections.constructor(
                    JoinedCrewResponse.class,
                    QCrew.crew.id,
                    QCrew.crew.title,
                    QCrew.crew.mainLocation,
                    QCrew.crew.subLocation,
                    JPAExpressions.select(QMemberCrew.memberCrew.count())
                        .from(QMemberCrew.memberCrew)
                        .where(QMemberCrew.memberCrew.crew.eq(QCrew.crew)),
                    QCrew.crew.totalCount.longValue(),
                    QCrew.crew.imageUrl,
                    JPAExpressions.select(QGathering.gathering.count())
                        .from(QGathering.gathering)
                        .where(QGathering.gathering.crew.eq(QCrew.crew))))
            .from(QMemberCrew.memberCrew)
            .join(QMemberCrew.memberCrew.crew, QCrew.crew)
            .where(
                QMemberCrew.memberCrew.member.email.eq(email),
                QMemberCrew.memberCrew.isCaptain.eq(false))
            .orderBy(QCrew.crew.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize() + 1)
            .fetch()
            .stream()
            .map(
                crew -> {
                  List<JoinedCrewResponse.CrewMemberResponse> members =
                      getCrewMembers(crew.getId());
                  return crew.withCrewMembers(members);
                })
            .collect(Collectors.toList());

    boolean hasNext = false;
    if (crews.size() > pageable.getPageSize()) {
      crews.remove(crews.size() - 1);
      hasNext = true;
    }

    return new SliceImpl<>(crews, pageable, hasNext);
  }

  @Override
  public Slice<JoinedCrewResponse> findCrewsByHost(String email, Pageable pageable) {
    List<JoinedCrewResponse> crews =
        queryFactory
            .select(
                Projections.constructor(
                    JoinedCrewResponse.class,
                    QCrew.crew.id,
                    QCrew.crew.title,
                    QCrew.crew.mainLocation,
                    QCrew.crew.subLocation,
                    JPAExpressions.select(QMemberCrew.memberCrew.count())
                        .from(QMemberCrew.memberCrew)
                        .where(QMemberCrew.memberCrew.crew.eq(QCrew.crew)),
                    QCrew.crew.totalCount.longValue(),
                    QCrew.crew.imageUrl,
                    JPAExpressions.select(QGathering.gathering.count())
                        .from(QGathering.gathering)
                        .where(QGathering.gathering.crew.eq(QCrew.crew))))
            .from(QMemberCrew.memberCrew)
            .join(QMemberCrew.memberCrew.crew, QCrew.crew)
            .where(
                QMemberCrew.memberCrew.member.email.eq(email),
                QMemberCrew.memberCrew.isCaptain.eq(true))
            .orderBy(QCrew.crew.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize() + 1)
            .fetch()
            .stream()
            .map(
                crew -> {
                  List<JoinedCrewResponse.CrewMemberResponse> members =
                      getCrewMembers(crew.getId());
                  return crew.withCrewMembers(members);
                })
            .collect(Collectors.toList());

    boolean hasNext = false;
    if (crews.size() > pageable.getPageSize()) {
      crews.remove(crews.size() - 1);
      hasNext = true;
    }

    return new SliceImpl<>(crews, pageable, hasNext);
  }

  @Override
  public Slice<CrewListResponse> searchCrews(CrewSearchCondition condition, Pageable pageable) {
    List<CrewListResponse> crews =
        queryFactory
            .select(
                Projections.constructor(
                    CrewListResponse.class,
                    QCrew.crew.id,
                    QCrew.crew.mainCategory.stringValue(),
                    QCrew.crew.subCategory.stringValue(),
                    QCrew.crew.title,
                    QCrew.crew.mainLocation,
                    QCrew.crew.subLocation,
                    ExpressionUtils.as(
                        JPAExpressions.select(QMemberCrew.memberCrew.count())
                            .from(QMemberCrew.memberCrew)
                            .where(QMemberCrew.memberCrew.crew.eq(QCrew.crew)),
                        "participantCount"),
                    QCrew.crew.totalCount,
                    QCrew.crew.imageUrl,
                    QCrew.crew.isConfirmed,
                    ExpressionUtils.as(
                        JPAExpressions.select(QGathering.gathering.count())
                            .from(QGathering.gathering)
                            .where(QGathering.gathering.crew.eq(QCrew.crew)),
                        "totalGatheringCount")))
            .from(QCrew.crew)
            .where(
                searchKeywordContains(condition.getKeyword()),
                mainLocationEq(condition.getMainLocation()),
                categoryEq(condition.getMainCategory(), condition.getSubCategory()))
            .orderBy(getSortCondition(condition.getSortType()))
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

  private BooleanExpression searchKeywordContains(String keyword) {
    if (StringUtils.isBlank(keyword)) {
      return null;
    }
    return QCrew.crew
        .title
        .containsIgnoreCase(keyword)
        .or(QCrew.crew.mainLocation.containsIgnoreCase(keyword))
        .or(QCrew.crew.subLocation.containsIgnoreCase(keyword));
  }

  private BooleanExpression mainLocationEq(String mainLocation) {
    return StringUtils.isBlank(mainLocation) ? null : QCrew.crew.mainLocation.eq(mainLocation);
  }

  private BooleanExpression categoryEq(String mainCategoryLabel, String subCategoryLabel) {
    if (StringUtils.isBlank(mainCategoryLabel) && StringUtils.isBlank(subCategoryLabel)) {
      return null;
    }

    BooleanExpression expression = null;

    if (StringUtils.isNotBlank(mainCategoryLabel)) {
      try {
        MainCategory mainCategory = MainCategory.fromLabel(mainCategoryLabel);
        expression = QCrew.crew.mainCategory.eq(mainCategory);
      } catch (IllegalArgumentException e) {
        log.warn("Invalid mainCategory label: {}", mainCategoryLabel);
      }
    }

    if (StringUtils.isNotBlank(subCategoryLabel)) {
      try {
        SubCategory subCategory = SubCategory.fromLabel(subCategoryLabel);
        BooleanExpression subCategoryExp = QCrew.crew.subCategory.eq(subCategory);
        expression = expression == null ? subCategoryExp : expression.and(subCategoryExp);
      } catch (IllegalArgumentException e) {
        log.warn("Invalid subCategory label: {}", subCategoryLabel);
      }
    }

    return expression;
  }

  private OrderSpecifier<?>[] getSortCondition(CrewSearchCondition.SortType sortType) {
    if (sortType == CrewSearchCondition.SortType.POPULAR) {
      return new OrderSpecifier[] {
        new OrderSpecifier<>(
            Order.DESC,
            JPAExpressions.select(QMemberCrew.memberCrew.count())
                .from(QMemberCrew.memberCrew)
                .where(QMemberCrew.memberCrew.crew.eq(QCrew.crew))),
        QCrew.crew.createdAt.desc()
      };
    }
    return new OrderSpecifier[] {QCrew.crew.createdAt.desc()};
  }

  private List<JoinedCrewResponse.CrewMemberResponse> getCrewMembers(Long crewId) {
    return queryFactory
        .select(
            Projections.constructor(
                JoinedCrewResponse.CrewMemberResponse.class,
                QMember.member.id,
                QMember.member.nickName,
                QMember.member.profileImageUrl,
                QMemberCrew.memberCrew.isCaptain))
        .from(QMemberCrew.memberCrew)
        .join(QMemberCrew.memberCrew.member, QMember.member)
        .where(QMemberCrew.memberCrew.crew.id.eq(crewId))
        .fetch();
  }
}
