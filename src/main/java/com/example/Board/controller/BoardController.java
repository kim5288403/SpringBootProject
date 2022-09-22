package com.example.Board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class BoardController {
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String main() {
		return "/main";
	}
	
	@RequestMapping(value = "/generic", method = RequestMethod.GET)
	public String generic() {
		return "elements";
	}
	
	@RequestMapping(value = "/elements", method = RequestMethod.GET)
	public String elements() {
		return "generic";
	}
}
