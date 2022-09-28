package com.example.Board.dto;


import javax.validation.constraints.NotNull;

import com.example.Board.entity.RefreshToken;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class TokenRequestDto {
	@NotNull
	private Long memberId;
	
	@NotNull
	private String refreshToken;
	
    @Builder
    public TokenRequestDto(Long memberId, String refreshToken) {
        this.memberId = memberId;
        this.refreshToken = refreshToken;
    }
    
	public static RefreshToken create (Long memberId, String refreshToken ) {
		RefreshToken refreshTokenEntity = RefreshToken.builder()
				.memberId(memberId)
				.refreshToken(refreshToken)
				.build();
		return refreshTokenEntity;
	}
	
	
}
