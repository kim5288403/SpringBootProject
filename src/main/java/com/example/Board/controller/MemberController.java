package com.example.Board.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/member")
@Controller
public class MemberController {

    
    @GetMapping(value ="/login")
    public String login() {
    	
    	return "/member/login";
    }
    
    @GetMapping(value = "/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요.");
        
        return "/member/login";
    }
    
    @GetMapping(value = "/join")
    public String join() {
    	
        return "/member/join";
    }
    
}