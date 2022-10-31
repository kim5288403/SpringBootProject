package com.example.Board.controller;



import java.io.IOException;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Board.dto.LoginRequestDto;
import com.example.Board.dto.LoginResponseDto;
import com.example.Board.dto.MemberRequestDto;
import com.example.Board.dto.MemberResponseDto;
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
   
	@GetMapping(value = "/kakao")
	public ResponseEntity<RestResponse<LoginResponseDto>> kakaoLogin(@RequestParam String code, Model model) throws IOException {
		log.info("로그인 시도됨");
	
		try {
			LoginResponseDto loginResponseDto = memberService.kakaoLogin(code, passwordEncoder, jwtTokenProvider);
			return new ResponseEntity<RestResponse<LoginResponseDto>>(RestResponse.res(StatusCode.OK, ResponseMessage.LOGIN_SUCCESS, loginResponseDto), HttpStatus.OK);	
		} catch (Exception e) {
			return new ResponseEntity<RestResponse<LoginResponseDto>>(RestResponse.res(StatusCode.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(value = "join")
	public ResponseEntity<RestResponse<MemberResponseDto>> join(@Valid @RequestBody MemberRequestDto memberDto, BindingResult bindingResult) {
		log.info("회원가입 시도됨");
		
		if (bindingResult.hasErrors()) {
			return new ResponseEntity<RestResponse<MemberResponseDto>>(RestResponse.res(StatusCode.BAD_REQUEST, ResponseMessage.INPUT_ERROR, new MemberResponseDto(memberDto)), HttpStatus.OK);
        }
		
		try {
	        Member member = MemberRequestDto.create(memberDto, passwordEncoder);
	        memberService.save(member);
		}catch (Exception e) {
			return new ResponseEntity<RestResponse<MemberResponseDto>>(RestResponse.res(StatusCode.BAD_REQUEST, e.getMessage(), new MemberResponseDto(memberDto)), HttpStatus.OK);
		}

		return new ResponseEntity<RestResponse<MemberResponseDto>>(RestResponse.res(StatusCode.CREATED, ResponseMessage.CREATED_USER , new MemberResponseDto(memberDto)), HttpStatus.OK);
	}
	
	@PostMapping(value = "login")
	public ResponseEntity<RestResponse<LoginResponseDto>> login(@RequestBody LoginRequestDto loginDto) {
		log.info("로그인 시도됨");
		
		try {
			LoginResponseDto loginResponseDto = memberService.login(loginDto, passwordEncoder, jwtTokenProvider);
			return new ResponseEntity<RestResponse<LoginResponseDto>>(RestResponse.res(StatusCode.OK, ResponseMessage.LOGIN_SUCCESS, loginResponseDto), HttpStatus.OK);	
		} catch (Exception e) {
			return new ResponseEntity<RestResponse<LoginResponseDto>>(RestResponse.res(StatusCode.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(value = "logout") 
	public <T> ResponseEntity<RestResponse<T>> logout (@RequestHeader(value="Authorization") String accessToken) {
		log.info("로그아웃 시도됨");
		
		try {
			RestResponse<T> res = memberService.logout(accessToken, jwtTokenProvider);
			return new ResponseEntity<RestResponse<T>>(RestResponse.res(StatusCode.OK, res.getResponseMessage()), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RestResponse<T>>(RestResponse.res(StatusCode.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
	
}
