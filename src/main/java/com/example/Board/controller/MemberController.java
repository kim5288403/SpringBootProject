package com.example.Board.controller;

import javax.validation.Valid;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.Board.dto.MemberDto;
import com.example.Board.entity.Member;
import com.example.Board.model.MemberService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/member")
@Controller
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    
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
    public String join(Model model) {
        model.addAttribute("memberDto", new MemberDto());
        return "/member/join";
    }
    
    @PostMapping(value = "join")
    public String join(@Valid MemberDto memberDto, BindingResult bindingResult, Model model) {
        
    	if (bindingResult.hasErrors()) {
            return "/member/join";
        }
       	
    	try {
            Member member = Member.create(memberDto, passwordEncoder);
            memberService.save(member);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "/member/join";
        }
    	
        return "redirect:/";
    }
}