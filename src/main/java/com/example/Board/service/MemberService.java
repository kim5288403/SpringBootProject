package com.example.Board.service;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Date;
import java.util.Map;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
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
import com.example.Board.event.AfterTransactionEvent;
import com.example.Board.event.BeforeTransactionEvent;
import com.example.Board.jwt.JwtTokenProvider;
import com.example.Board.restfull.RestResponse;
import com.example.Board.restfull.StatusCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class MemberService implements UserDetailsService{

	private final MemberRepository memberRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final RedisUtil redisUtil;
	private final KaKaoService kakaoService;
	private final ApplicationEventPublisher applicationEventPublisher; 
	

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

	public Member save(Member member) {
		validateDuplicateMember(member);
		Member check = memberRepository.save(member);
		BeforeTransactionEvent beforeTransactionEvent = () -> log.info("commit ??????, {}", LocalTime.now());
		AfterTransactionEvent afterTransactionEvent = new AfterTransactionEvent() {
			
			@Override
			public void callback() {
				// TODO Auto-generated method stub
				System.out.println("???????????? ??? " +  LocalTime.now());
			}
			
			@Override
			public void completed() {
				// TODO Auto-generated method stub
				System.out.println("commit ??????" + LocalTime.now());
			}
		};
		
		applicationEventPublisher.publishEvent(beforeTransactionEvent);
		applicationEventPublisher.publishEvent(afterTransactionEvent);
		
		return check;
	}
	
	@Cacheable(value = "Member", key = "#member.getEmail()", cacheManager = "projectCacheManager")
	private void validateDuplicateMember(Member member) {
		Member findMember = memberRepository.findByEmail(member.getEmail());
		if (findMember != null) {
			throw new IllegalStateException("?????? ????????? ???????????????.");
		}
	}

	@Cacheable(value = "Member", key = "#loginDto.getEmail()", cacheManager = "projectCacheManager")
	public LoginResponseDto login(LoginRequestDto loginDto, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
		Member member = memberRepository.findByEmail(loginDto.getEmail());
		validateDuplicateMemberLogin(member, loginDto, passwordEncoder);

		String accessToken = jwtTokenProvider.createAccessToken(member.getEmail(), member.getRole( ) + "");
		String refreshToken = jwtTokenProvider.createRefreshToken(member.getId() + "");

		RefreshToken refreshTokenEntity = TokenRequestDto.create(member.getId(), refreshToken);
		refreshTokenRepository.save(refreshTokenEntity);

		return new LoginResponseDto(loginDto.getEmail(), accessToken,  refreshToken);
	}
	
	private void validateDuplicateMemberLogin(Member member, LoginRequestDto loginDto, PasswordEncoder passwordEncoder) {
		if (member == null) {
			throw new BadCredentialsException("????????? ?????????");
		}

		if (!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())) {
			throw new BadCredentialsException("???????????? ?????????");
		}
	}

	public <T> RestResponse<T> logout(String accessToken, JwtTokenProvider jwtTokenProvider) {
		if (jwtTokenProvider.validateToken(accessToken)) {
			Member member = memberRepository.findByEmail(jwtTokenProvider.getUserPk(accessToken));
			refreshTokenRepository.deleteById(member.getId());
			
			Date expiration = jwtTokenProvider.getUserExpiration(accessToken);
			Date now = new Date();
			redisUtil.setBlackList(accessToken, "access_token", expiration.getTime() - now.getTime());
			
			return RestResponse.res(StatusCode.OK, "???????????? ??????");
		}else {
			return RestResponse.res(StatusCode.BAD_REQUEST, "?????????????????? ???????????????.");
		}
	}
	
	public LoginResponseDto kakaoLogin(String code, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
		try {
			String kakaoAccessToken = kakaoService.getToken(code);
			Map<String, Object> userInfo = kakaoService.getUserInfo(kakaoAccessToken);
			String userEmail = userInfo.get("email") + "";
			Member findMember = memberRepository.findByEmail(userEmail);
			Long userId;
			
			if (findMember == null) {
				MemberRequestDto memberDto = new MemberRequestDto(userInfo.get("nickname").toString(), userEmail, null, null, null, userInfo.get("gender").toString());
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
			throw new IllegalStateException("????????? ????????? ??????");
		}
	}
}