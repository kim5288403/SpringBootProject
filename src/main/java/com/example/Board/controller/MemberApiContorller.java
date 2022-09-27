package com.example.Board.controller;


import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Board.dto.LoginDto;
import com.example.Board.dto.MemberDto;
import com.example.Board.entity.Member;
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
	public ResponseEntity<RestResponse<LoginDto>> login(@RequestBody LoginDto loginDto) {
		log.info("로그인 시도됨");
		
		try {
			LoginDto loginInfo = memberService.login(loginDto, passwordEncoder, jwtTokenProvider);
			return new ResponseEntity<RestResponse<LoginDto>>(RestResponse.res(StatusCode.OK, ResponseMessage.LOGIN_SUCCESS, loginInfo), HttpStatus.OK);	
		} catch (Exception e) {
			return new ResponseEntity<RestResponse<LoginDto>>(RestResponse.res(StatusCode.BAD_REQUEST, ResponseMessage.LOGIN_FAIL, loginDto), HttpStatus.OK);
		}
	}
	
}
