package com.example.Board.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RefreshToken {
	
	@Id
	private Long memberId;
	
	private String refreshToken;
	
	private LocalDateTime createdDate = LocalDateTime.now();
	
	private LocalDateTime modifiedDate;
	
	@Builder
	public RefreshToken (Long memberId, String refreshToken) {
		this.memberId = memberId;
		this.refreshToken = refreshToken;
	}
	
	public void update(String refreshToken) {
		this.refreshToken = refreshToken;
		this.modifiedDate = LocalDateTime.now();
	}
	
}
