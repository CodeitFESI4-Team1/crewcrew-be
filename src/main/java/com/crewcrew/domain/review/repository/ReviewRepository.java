package com.crewcrew.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crewcrew.domain.review.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewCustomRepository {}
