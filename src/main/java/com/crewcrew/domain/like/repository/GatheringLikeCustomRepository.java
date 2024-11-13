package com.crewcrew.domain.like.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.crewcrew.domain.like.dto.GatheringLikeResponse;

public interface GatheringLikeCustomRepository {
  Page<GatheringLikeResponse.GatheringLikeList> getMemberLikes(Long userId, Pageable pageable);
}
