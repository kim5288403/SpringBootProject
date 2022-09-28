package com.example.Board.entity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>{
	RefreshToken findByMemberId(Long id);

}
