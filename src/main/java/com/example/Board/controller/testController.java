package com.example.Board.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testController {
	
    @PostMapping("/test")
    public String test(){
        return "<h1>test 통과</h1>";
    }
}
