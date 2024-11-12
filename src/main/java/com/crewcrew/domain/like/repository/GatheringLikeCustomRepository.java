package com.crewcrew.domain.like.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.crewcrew.domain.like.dto.GatheringLikeResponse;

public interface GatheringLikeCustomRepository {
  Slice<GatheringLikeResponse.GatheringLikeList> getMemberLikes(Long userId, Pageable pageable);
}
