package com.example.Board.model;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Board.dto.LoginRequestDto;
import com.example.Board.dto.LoginResponseDto;
import com.example.Board.dto.TokenRequestDto;
import com.example.Board.entity.Member;
import com.example.Board.entity.MemberRepository;
import com.example.Board.entity.RefreshToken;
import com.example.Board.entity.RefreshTokenRepository;
import com.example.Board.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService implements UserDetailsService{

	private final MemberRepository memberRepository;
	private final RefreshTokenRepository refreshTokenRepository;

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

	private void validateDuplicateMember(Member member) {
		Member findMember = memberRepository.findByEmail(member.getEmail());
		if (findMember != null) {
			throw new IllegalStateException("이미 가입된 회원입니다.");
		}
	}

	//로그인
	public LoginResponseDto login(LoginRequestDto loginDto, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
		Member member = memberRepository.findByEmail(loginDto.getEmail());
		validateDuplicateMemberLogin(member, loginDto, passwordEncoder);

		RefreshToken refreshTokenEntity = TokenRequestDto.create(member.getId(), jwtTokenProvider.createRefreshToken());
		refreshTokenRepository.save(refreshTokenEntity);

		String token = jwtTokenProvider.createToken(member.getEmail(), member.getRole()+"");
		String refreshToken = jwtTokenProvider.createRefreshToken();

		return new LoginResponseDto(loginDto.getEmail(), token,  refreshToken);
	}


	private void validateDuplicateMemberLogin(Member member, LoginRequestDto loginDto, PasswordEncoder passwordEncoder) {
		if (member == null) {
			throw new BadCredentialsException("이메일 불일치 : " + loginDto.getEmail());
		}

		if (!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())) {
			throw new BadCredentialsException("비밀번호 불일치 : " + member.getPassword());
		}

	}
}