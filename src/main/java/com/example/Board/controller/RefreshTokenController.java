package com.example.Board.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Board.dto.TokenResponseDto;
import com.example.Board.model.TokenService;
import com.example.Board.restfull.RestResponse;
import com.example.Board.restfull.StatusCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/token")
@RestController
@RequiredArgsConstructor
public class RefreshTokenController {
	private final TokenService tokenService;
	
	@PostMapping(value = "/refresh") 
	public  ResponseEntity<RestResponse<TokenResponseDto>> refresh(@RequestHeader(value="RefreshToken") String refreshToken) {
		log.info("accessToken 재발급 시도됨");
		try {
			TokenResponseDto tokenResponseDto = tokenService.refresh(refreshToken);
			return new ResponseEntity<RestResponse<TokenResponseDto>>(RestResponse.res(StatusCode.OK, "재발급 성공하였습니다.", tokenResponseDto),HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RestResponse<TokenResponseDto>>(RestResponse.res(StatusCode.BAD_REQUEST, "재발급 실패하였습니다."+e.getMessage()),HttpStatus.BAD_REQUEST);
		}

	}

}
