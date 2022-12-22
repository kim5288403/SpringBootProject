package com.example.Board.vaildator;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.Board.entity.Member;
import com.example.Board.entity.MemberRepository;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberValidator{
	
	private final MemberRepository memberRepository;
	
	@Cacheable(value = "Member", key = "email", cacheManager = "projectCacheManager")
	public Member validateDuplicateMember(String email) throws UsernameNotFoundException{
		Member findMember = memberRepository.findByEmail(email);
		
		if (findMember != null) {
			throw new UsernameNotFoundException("이미 가입된 회원입니다.");
		}
		
		return findMember;
	}
	
	@Cacheable(value = "Member", key = "phone", cacheManager = "projectCacheManager")
	public void validateDuplicatePhone(String phone) {
		Member findPhone = memberRepository.findByPhone(phone);
		
		if (findPhone != null) {
			throw new IllegalStateException("이미 가입된 핸드폰 번호입니다.");
		}
	}
	
	@Cacheable(value = "Member", key = "email", cacheManager = "projectCacheManager")
	public Member validateDuplicateEmail(String email) {
		Member findMember = memberRepository.findByEmail(email);
		
		if (findMember == null) {
			throw new BadCredentialsException("이메일 불일치");
		}
		
		return findMember;
	}
	
	public void validateDuplicatePassword(String memberPassword, String password, PasswordEncoder passwordEncoder) {
		
		if (!passwordEncoder.matches(password, memberPassword)) {
			throw new BadCredentialsException("비밀번호 불일치");
		}
	}

}
