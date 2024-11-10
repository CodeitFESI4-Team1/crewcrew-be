package com.crewcrew.domain.gathering.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.crewcrew.domain.gathering.dto.response.GatheringListResponse;
import com.crewcrew.domain.gathering.entity.Gathering;

@Repository
public interface GatheringRepository extends JpaRepository<Gathering, Long> {
  Optional<Gathering> findByIdAndCrewId(Long id, Long crewId);

  boolean existsByCrewIdAndDateTime(Long crewId, LocalDateTime dateTime);

  @Query(
      "SELECT new com.crewcrew.domain.gathering.dto.response.GatheringListResponse("
          + "g.id, g.title, g.introduce, g.dateTime, g.location, g.imageUrl, "
          + "g.totalCount, "
          + "(SELECT COUNT(gp) FROM GatheringParticipant gp WHERE gp.gathering = g), "
          + "CASE WHEN EXISTS (SELECT 1 FROM GatheringLike gl WHERE gl.gathering = g AND gl.member.id = :memberId) "
          + "THEN true ELSE false END) "
          + "FROM Gathering g "
          + "WHERE g.crew.id = :crewId "
          + "AND g.dateTime > :now "
          + "ORDER BY g.dateTime DESC")
  List<GatheringListResponse> findAllByCrewIdWithLikeInfo(
      @Param("crewId") Long crewId,
      @Param("memberId") Long memberId,
      @Param("now") LocalDateTime now);

  @Query(
      "SELECT new com.crewcrew.domain.gathering.dto.response.GatheringListResponse("
          + "g.id, g.title, g.introduce, g.dateTime, g.location, g.imageUrl, "
          + "g.totalCount, "
          + "(SELECT COUNT(gp) FROM GatheringParticipant gp WHERE gp.gathering = g), "
          + "false) "
          + "FROM Gathering g "
          + "WHERE g.crew.id = :crewId "
          + "AND g.dateTime > :now "
          + "ORDER BY g.dateTime DESC")
  List<GatheringListResponse> findAllByCrewId(
      @Param("crewId") Long crewId, @Param("now") LocalDateTime now);

  @Query(
      "SELECT new com.crewcrew.domain.gathering.dto.response.GatheringListResponse("
          + "g.id, g.title, g.introduce, g.dateTime, g.location, g.imageUrl, "
          + "g.totalCount, "
          + "(SELECT COUNT(gp) FROM GatheringParticipant gp WHERE gp.gathering = g), "
          + "CASE WHEN EXISTS (SELECT 1 FROM GatheringLike gl WHERE gl.gathering = g AND gl.member.id = :memberId) "
          + "THEN true ELSE false END) "
          + "FROM Gathering g "
          + "WHERE EXISTS (SELECT 1 FROM GatheringParticipant gp WHERE gp.gathering = g "
          + "   AND gp.member.id = :memberId AND gp.isGatheringCaptain = true) "
          + "AND g.dateTime > :now "
          + "ORDER BY g.dateTime DESC")
  List<GatheringListResponse> findAllByMemberAndIsCaptain(
      @Param("memberId") Long memberId, @Param("now") LocalDateTime now);
}
