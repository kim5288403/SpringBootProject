package com.example.Board.Vaildator;

import org.springframework.cache.annotation.Cacheable;

import com.example.Board.entity.Member;
import com.example.Board.entity.MemberRepository;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberValidator{
	
	private final MemberRepository memberRepository;
	
	@Cacheable(value = "Member", key = "#member.getEmail()", cacheManager = "projectCacheManager")
	public void validateDuplicateMember(Member member) {
		Member findMember = memberRepository.findByEmail(member.getEmail());
		
		if (findMember != null) {
			throw new IllegalStateException("이미 가입된 회원입니다.");
		}
		
		Member findPhone = memberRepository.findByPhone(member.getPhone());
		
		if (findPhone != null) {
			throw new IllegalStateException("이미 가입된 핸드폰 번호입니다.");
		}
		
	}


}
