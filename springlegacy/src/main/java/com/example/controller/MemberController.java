package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member/*")
public class MemberController {
	
	@GetMapping("/login")
	public String loginForm() {
		
		System.out.println("loginForm() 호출됨...");
		
		return "member/login";
	}
}
