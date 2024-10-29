package com.crewcrew.domain.crew.repository;

import static com.crewcrew.domain.crew.entity.QCrew.crew;

import java.util.*;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import com.crewcrew.domain.crew.dto.request.CrewFss;
import com.crewcrew.domain.crew.entity.Crew;
import com.crewcrew.domain.crew.enums.*;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@AllArgsConstructor
public class CrewCustomRepositoryImpl implements CrewCustomRepository {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public Slice<Crew> findFilteredCrews(CrewFss fss, Pageable pageable) {
    List<Crew> entities =
        jpaQueryFactory
            .selectFrom(crew)
            .where(
                condCanceled(),
                condKeyword(fss.searchKeyword()),
                condType(fss.type()),
                condSubType(fss.subType()),
                condLocation(fss.location()),
                conddetailedLocation(fss.detailedLocation()))
            .orderBy(getOrderSpecifier(pageable.getSort()))
            .fetch();

    return new SliceImpl<>(entities, pageable, entities.size() == pageable.getPageSize());
  }

  // WHERE
  private BooleanExpression condSubType(String subType) {
    return subType == null ? null : crew.subType.eq(SubCategory.valueOf(subType));
  }

  private BooleanExpression condLocation(String location) {
    return location == null ? null : crew.location.contains(location);
  }

  private BooleanExpression conddetailedLocation(String detailedLocation) {
    return detailedLocation == null ? null : crew.detailedLocation.contains(detailedLocation);
  }

  private BooleanExpression condKeyword(String keyword) {
    return keyword == null ? null : crew.name.contains(keyword);
  }

  private BooleanExpression condCanceled() {
    return crew.canceledAt.isNull();
  }

  private BooleanExpression condType(String type) {
    return type == null ? null : crew.type.eq(Category.valueOf(type.toUpperCase()));
  }

  // ORDER
  private OrderSpecifier<?>[] getOrderSpecifier(Sort sort) {
    List<OrderSpecifier> orders = new ArrayList<>();

    if (sort.isEmpty()) {
      orders.add(new OrderSpecifier<>(Order.DESC, crew.updatedAt));
    }

    sort.forEach(
        s -> {
          String property = s.getProperty();
          Order direction = s.isAscending() ? Order.ASC : Order.DESC;
          log.debug("{}, {}", property, direction);

          switch (property) {
            case "id" -> orders.add(new OrderSpecifier<>(direction, crew.id));
            case "name" -> orders.add(new OrderSpecifier<>(direction, crew.name));
            case "type" -> orders.add(new OrderSpecifier<>(direction, crew.type));
            case "subType" -> orders.add(new OrderSpecifier<>(direction, crew.subType));
            case "description" -> orders.add(new OrderSpecifier<>(direction, crew.description));
            case "location" -> orders.add(new OrderSpecifier<>(direction, crew.location));
            case "detailedLocation" -> orders.add(
                new OrderSpecifier<>(direction, crew.detailedLocation));
            case "capacity" -> orders.add(new OrderSpecifier<>(direction, crew.capacity));
            case "participantCount" -> orders.add(
                new OrderSpecifier<>(direction, crew.participantCount));
            case "canceledAt" -> orders.add(new OrderSpecifier<>(direction, crew.canceledAt));
            case "updatedAt" -> orders.add(new OrderSpecifier<>(direction, crew.updatedAt));
            case "createdAt" -> orders.add(new OrderSpecifier<>(direction, crew.createdAt));
            case "isConfirmed" -> orders.add(new OrderSpecifier<>(direction, crew.isConfirmed));
          }
        });

    return orders.toArray(OrderSpecifier[]::new);
  }
}
