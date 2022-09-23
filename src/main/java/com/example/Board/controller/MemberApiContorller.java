package com.example.Board.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Board.restfull.ResponseMessage;
import com.example.Board.restfull.RestResponse;
import com.example.Board.restfull.StatusCode;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/member")
@RestController
@RequiredArgsConstructor
public class MemberApiContorller {
	
	@PostMapping("join")
	public ResponseEntity join(@RequestBody Map<String, String> map ) {
		return new ResponseEntity(RestResponse.res(StatusCode.OK,ResponseMessage.CREATED_USER , map), HttpStatus.OK);
	}
	
}
