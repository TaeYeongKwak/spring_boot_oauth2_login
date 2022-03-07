package com.taeyeong.login.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taeyeong.login.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>{
	Optional<Member> findByEmail(String email);
}
