package com.example.Board.model;



import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Board.auth.RedisUtil;
import com.example.Board.dto.LoginRequestDto;
import com.example.Board.dto.LoginResponseDto;
import com.example.Board.dto.MemberRequestDto;
import com.example.Board.dto.TokenRequestDto;
import com.example.Board.entity.Member;
import com.example.Board.entity.MemberRepository;
import com.example.Board.entity.RefreshToken;
import com.example.Board.entity.RefreshTokenRepository;
import com.example.Board.jwt.JwtTokenProvider;
import com.example.Board.restfull.RestResponse;
import com.example.Board.restfull.StatusCode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService implements UserDetailsService{

	private final MemberRepository memberRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final RedisUtil redisUtil;
	private final KaKaoService kakaoService;

	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Member member = memberRepository.findByEmail(email);

		if (member == null) {
			throw new UsernameNotFoundException(email);
		}

		return User.builder()
				.username(member.getEmail())
				.password(member.getPassword())
				.roles(member.getRole().toString())
				.build();
	}

	//회원가입 
	public Member save(Member member) {
		validateDuplicateMember(member);
		return memberRepository.save(member);
	}
	
	@Cacheable(value = "Member", key="#member.getEmail()", cacheManager = "projectCacheManager")
	private void validateDuplicateMember(Member member) {
		Member findMember = memberRepository.findByEmail(member.getEmail());
		if (findMember != null) {
			throw new IllegalStateException("이미 가입된 회원입니다.");
		}
	}

	//로그인
	@Cacheable(value = "Member", key="#loginDto.getEmail()", cacheManager = "projectCacheManager")
	public LoginResponseDto login(LoginRequestDto loginDto, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
		Member member = memberRepository.findByEmail(loginDto.getEmail());
		validateDuplicateMemberLogin(member, loginDto, passwordEncoder);

		String accessToken = jwtTokenProvider.createAccessToken(member.getEmail(), member.getRole()+"");
		String refreshToken = jwtTokenProvider.createRefreshToken(member.getId()+"");

		RefreshToken refreshTokenEntity = TokenRequestDto.create(member.getId(), refreshToken);
		refreshTokenRepository.save(refreshTokenEntity);

		return new LoginResponseDto(loginDto.getEmail(), accessToken,  refreshToken);
	}
	
	private void validateDuplicateMemberLogin(Member member, LoginRequestDto loginDto, PasswordEncoder passwordEncoder) {
		if (member == null) {
			throw new BadCredentialsException("이메일 불일치");
		}

		if (!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())) {
			throw new BadCredentialsException("비밀번호 불일치");
		}
	}

	//로그아웃
	public <T> RestResponse<T> logout(String accessToken, JwtTokenProvider jwtTokenProvider) {
		if (jwtTokenProvider.validateToken(accessToken)) {
			Member member = memberRepository.findByEmail(jwtTokenProvider.getUserPk(accessToken));
			refreshTokenRepository.deleteById(member.getId());
			Date expiration = jwtTokenProvider.getUserExpiration(accessToken);
			Date now = new Date();

			redisUtil.setBlackList(accessToken, "access_token", expiration.getTime() - now.getTime());
			return RestResponse.res(StatusCode.OK, "로그아웃 성공");
		}else {
			return RestResponse.res(StatusCode.BAD_REQUEST, "유효하지않은 토큰입니다.");
		}
	}
	
	//카카오 로그인
	public LoginResponseDto kakaoLogin(String code, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
		try {
			String kakaoAccessToken = kakaoService.getToken(code);
			Map<String, Object> userInfo = kakaoService.getUserInfo(kakaoAccessToken);
			String userEmail = userInfo.get("email")+"";
			Member findMember = memberRepository.findByEmail(userEmail);
			Long userId;
			
			if (findMember == null) {
				MemberRequestDto memberDto = new MemberRequestDto(userInfo.get("nickname").toString(), userEmail, "", "", userInfo.get("gender").toString());
				Member saveMember = MemberRequestDto.create(memberDto, passwordEncoder);
				userId = memberRepository.save(saveMember).getId();
			}else {
				userId = findMember.getId();
			}
			
			String accessToken = jwtTokenProvider.createAccessToken(userEmail, "USER");
			String refreshToken = jwtTokenProvider.createRefreshToken(userEmail);
	
			RefreshToken refreshTokenEntity = TokenRequestDto.create(userId, refreshToken);
			refreshTokenRepository.save(refreshTokenEntity);
			
			return new LoginResponseDto(userEmail, accessToken, refreshToken);
		} catch (IOException e) {
			throw new IllegalStateException("카카오 로그인 실패");
		}
	}


}