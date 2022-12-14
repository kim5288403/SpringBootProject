package com.example.Board.entity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long>{
	Member findByEmail(String email);
	Member findByPhone(String phone);
	Optional<Member> findById(Long id);
}
