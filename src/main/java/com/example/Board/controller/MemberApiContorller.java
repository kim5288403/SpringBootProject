package com.example.Board.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Board.dto.MemberDto;
import com.example.Board.dto.loginDto;
import com.example.Board.entity.Member;
import com.example.Board.entity.MemberRepository;
import com.example.Board.jwt.JwtTokenProvider;
import com.example.Board.model.MemberService;
import com.example.Board.restfull.ResponseMessage;
import com.example.Board.restfull.RestResponse;
import com.example.Board.restfull.StatusCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/member")
@RestController
@RequiredArgsConstructor
public class MemberApiContorller {
	
	private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    
	@PostMapping(value = "join")
	public ResponseEntity<RestResponse<MemberDto>> join(@Valid @RequestBody MemberDto memberDto, BindingResult bindingResult) {
		log.info("회원가입 시도됨");
		
		if (bindingResult.hasErrors()) {
			return new ResponseEntity<RestResponse<MemberDto>>(RestResponse.res(StatusCode.BAD_REQUEST, ResponseMessage.INPUT_ERROR, memberDto), HttpStatus.OK);
        }
		
		try {
	        Member member = Member.create(memberDto, passwordEncoder);
	        memberService.save(member);
		}catch (Exception e) {
			return new ResponseEntity<RestResponse<MemberDto>>(RestResponse.res(StatusCode.BAD_REQUEST, ResponseMessage.DUPLICATION_USER , memberDto), HttpStatus.OK);
		}

		return new ResponseEntity<RestResponse<MemberDto>>(RestResponse.res(StatusCode.CREATED, ResponseMessage.CREATED_USER , memberDto), HttpStatus.OK);
	}
	
	@PostMapping(value = "login")
	public ResponseEntity<RestResponse<loginDto>> login(@RequestBody loginDto loginDto) {
		log.info("로그인 시도됨");
		
		Member member = memberRepository.findByEmail(loginDto.getEmail());
			
		if (member == null) {
			return new ResponseEntity<RestResponse<loginDto>>(RestResponse.res(StatusCode.BAD_REQUEST, ResponseMessage.LOGIN_FAIL, loginDto), HttpStatus.OK);
		}
		
		if (!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())) {
			return new ResponseEntity<RestResponse<loginDto>>(RestResponse.res(StatusCode.BAD_REQUEST, ResponseMessage.LOGIN_FAIL, loginDto), HttpStatus.OK);
		}
		
		String token = jwtTokenProvider.createToken(member.getEmail(), member.getRole()+"");
		loginDto.setToken(token);
		return new ResponseEntity<RestResponse<loginDto>>(RestResponse.res(StatusCode.OK, ResponseMessage.LOGIN_SUCCESS, loginDto), HttpStatus.OK);
	}
	
}
