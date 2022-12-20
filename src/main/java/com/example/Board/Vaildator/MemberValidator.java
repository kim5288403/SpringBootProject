package com.example.Board.Vaildator;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.Board.entity.Member;
import com.example.Board.entity.MemberRepository;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberValidator{
	
	private final MemberRepository memberRepository;
	
	@Cacheable(value = "Member", key = "#member.getEmail()", cacheManager = "projectCacheManager")
	public Member validateDuplicateMember(String email) throws UsernameNotFoundException{
		Member findMember = memberRepository.findByEmail(email);
		
		if (findMember != null) {
			throw new UsernameNotFoundException("이미 가입된 회원입니다.");
		}
		
		return findMember;
	}
	
	public void validateDuplicatePhone(String phone) {
		Member findPhone = memberRepository.findByPhone(phone);
		
		if (findPhone != null) {
			throw new IllegalStateException("이미 가입된 핸드폰 번호입니다.");
		}
	}


}
