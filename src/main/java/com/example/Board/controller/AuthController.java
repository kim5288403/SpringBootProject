package com.example.Board.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Board.model.KaKaoService;


@Controller
@RequestMapping(value="/member")
public class AuthController {
	
	@Autowired
	KaKaoService kakaoService;
	
	@GetMapping(value = "")
	public String login() {
		return "login";
	}
	
	@GetMapping(value = "/kakao")
	public String kakaoLogin(@RequestParam String code, Model model) throws IOException {
		System.out.println("code : " + code);
		
		String access_token = kakaoService.getToken(code);
		Map<String, Object> userInfo = kakaoService.getUserInfo(access_token);
		String getAgreementInfo = kakaoService.getAgreementInfo(access_token);

		System.out.println("access_token : " + access_token);
		System.out.println("userInfo : " + userInfo);
		System.out.println("getAgreementInfo : " + getAgreementInfo);
		
		model.addAttribute("code", code);
	    model.addAttribute("access_token", access_token);
	    model.addAttribute("userInfo", userInfo);
	     
		return "main";
	}
}
