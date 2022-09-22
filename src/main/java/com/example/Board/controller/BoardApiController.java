package com.example.Board.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Board.exception.CustomException;
import com.example.Board.exception.ErrorCode;

@RestController
@RequestMapping("/api")
public class BoardApiController {
	@GetMapping("/test")
	public String test() {
		throw new CustomException(ErrorCode.POSTS_NOT_FOUND);
	}
}
