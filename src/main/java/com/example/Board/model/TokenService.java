package com.example.Board.model;

import java.util.Optional;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Board.dto.TokenResponseDto;
import com.example.Board.entity.Member;
import com.example.Board.entity.MemberRepository;
import com.example.Board.entity.RefreshToken;
import com.example.Board.entity.RefreshTokenRepository;
import com.example.Board.jwt.JwtTokenProvider;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class TokenService {
	
	private final RefreshTokenRepository refreshTokenRepository;
	private final MemberRepository memberRepository;
	private final JwtTokenProvider jwtTokenProvider;

	public TokenResponseDto refresh(String requestRefreshToken) {
		String userPk = jwtTokenProvider.getUserPk(requestRefreshToken);
		Long MemberId = Long.parseLong(userPk);
		RefreshToken refreshTokenEntity = refreshTokenRepository.findByMemberId(MemberId);
		
		if (!jwtTokenProvider.validateToken(requestRefreshToken)) {
			throw new JwtException(" 유효하지않은 토큰입니다.");
		}else if (!requestRefreshToken.equals(refreshTokenEntity.getRefreshToken())) {
			throw new IllegalStateException(" 존재하지 않는 정보입니다.");
		} else {
			Optional<Member> member = memberRepository.findById(MemberId);
			String accessToken = jwtTokenProvider.createToken(member.get().getEmail(), member.get().getRole()+"");
			String refreshToken = jwtTokenProvider.createRefreshToken(member.get().getId()+"");
			refreshTokenEntity.update(refreshToken);
			
			return new TokenResponseDto(accessToken,refreshToken);
		}
	}

}
