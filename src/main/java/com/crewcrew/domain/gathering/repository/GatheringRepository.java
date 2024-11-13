package com.crewcrew.domain.gathering.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.crewcrew.domain.gathering.dto.response.GatheringListResponse;
import com.crewcrew.domain.gathering.dto.response.GatheringReviewResponse;
import com.crewcrew.domain.gathering.dto.response.MyGatheringListResponse;
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
      "SELECT new com.crewcrew.domain.gathering.dto.response.MyGatheringListResponse("
          + "g.id, g.crew.id, g.crew.title, g.crew.mainLocation, g.crew.subLocation, "
          + "g.title, g.introduce, g.dateTime, g.location, g.imageUrl, "
          + "(SELECT COUNT(gp) FROM GatheringParticipant gp WHERE gp.gathering = g), "
          + "g.totalCount, "
          + "CASE WHEN EXISTS (SELECT 1 FROM GatheringLike gl WHERE gl.gathering = g AND gl.member.id = :memberId) "
          + "THEN true ELSE false END) "
          + "FROM Gathering g "
          + "WHERE EXISTS (SELECT 1 FROM GatheringParticipant gp WHERE gp.gathering = g "
          + "   AND gp.member.id = :memberId AND gp.isGatheringCaptain = true) "
          + "AND g.dateTime > :startDateTime "
          + "ORDER BY g.dateTime DESC")
  List<MyGatheringListResponse> findAllByMemberAndIsCaptain(
      @Param("memberId") Long memberId, @Param("startDateTime") LocalDateTime startDateTime);

  @Query(
      "SELECT new com.crewcrew.domain.gathering.dto.response.MyGatheringListResponse("
          + "g.id, g.crew.id, g.crew.title, g.crew.mainLocation, g.crew.subLocation, "
          + "g.title, g.introduce, g.dateTime, g.location, g.imageUrl, "
          + "(SELECT COUNT(gp) FROM GatheringParticipant gp WHERE gp.gathering = g), "
          + "g.totalCount, "
          + "CASE WHEN EXISTS (SELECT 1 FROM GatheringLike gl WHERE gl.gathering = g AND gl.member.id = :memberId) "
          + "THEN true ELSE false END) "
          + "FROM Gathering g "
          + "WHERE EXISTS (SELECT 1 FROM GatheringParticipant gp WHERE gp.gathering = g "
          + "   AND gp.member.id = :memberId AND gp.isGatheringCaptain = false) "
          + "AND g.dateTime > :startDateTime "
          + "ORDER BY g.dateTime DESC")
  List<MyGatheringListResponse> findAllParticipatedGatherings(
      @Param("memberId") Long memberId, @Param("startDateTime") LocalDateTime startDateTime);

  @Modifying
  @Query("DELETE FROM Gathering g WHERE g.crew.id = :crewId")
  void deleteByCrewId(@Param("crewId") Long crewId);

  @Query(
      "SELECT new com.crewcrew.domain.gathering.dto.response.GatheringReviewResponse("
          + "g.id, g.title, g.dateTime, g.location, "
          + "(SELECT COUNT(gp) FROM GatheringParticipant gp WHERE gp.gathering = g), "
          + "g.totalCount, g.imageUrl, "
          + "(SELECT new com.crewcrew.domain.gathering.dto.response.GatheringParticipantResponse("
          + "    m.id, m.profileImageUrl) "
          + " FROM GatheringParticipant gp JOIN gp.member m "
          + " WHERE gp.gathering = g "
          + " ORDER BY gp.createdAt DESC)) "
          + "FROM Gathering g "
          + "WHERE EXISTS (SELECT 1 FROM GatheringParticipant gp WHERE gp.gathering = g "
          + "   AND gp.member.id = :memberId AND gp.isGatheringCaptain = false) "
          + "AND g.dateTime < :now "
          + "ORDER BY g.dateTime DESC")
  Slice<GatheringReviewResponse> findAllReviewableGatherings(
      @Param("memberId") Long memberId, @Param("now") LocalDateTime now, Pageable pageable);
}
