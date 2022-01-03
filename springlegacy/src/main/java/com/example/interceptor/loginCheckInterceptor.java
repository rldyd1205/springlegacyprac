package com.example.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import com.example.service.MemberService;

public class loginCheckInterceptor implements HandlerInterceptor{
	
	private MemberService memberService;

	public loginCheckInterceptor(MemberService memberService) {
		super();
		this.memberService = memberService;
	}
	
	
}
