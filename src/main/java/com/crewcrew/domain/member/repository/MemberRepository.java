package com.crewcrew.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crewcrew.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {}
