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

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class TokenService {
	
	private final RefreshTokenRepository refreshTokenRepository;
	private final MemberRepository memberRepository;
	private final JwtTokenProvider jwtTokenProvider;

	public TokenResponseDto refresh(String refreshToken) {
		String userPk = jwtTokenProvider.getUserPk(refreshToken);
		Long MemberId = Long.parseLong(userPk);
		RefreshToken refreshTokenEntity = refreshTokenRepository.findByMemberId(MemberId);

		if (refreshToken.equals(refreshTokenEntity.getRefreshToken()) && jwtTokenProvider.validateToken(refreshToken)) {
			Optional<Member> member = memberRepository.findById(MemberId);
			String accessToken = jwtTokenProvider.createToken(member.get().getEmail(), member.get().getRole()+"");
			String refreshToken2 = jwtTokenProvider.createRefreshToken(member.get().getId()+"");
			System.out.println(jwtTokenProvider.validateToken(accessToken));
			return new TokenResponseDto(accessToken,refreshToken2);
		}else {
			throw new BadCredentialsException("애러");
		}


		//		refreshTokenEntity.update(jwtTokenProvider.createRefreshToken());
	}

}
