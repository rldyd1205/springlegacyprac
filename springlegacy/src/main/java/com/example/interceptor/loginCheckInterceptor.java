package com.example.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

import com.example.service.MemberService;

public class loginCheckInterceptor implements HandlerInterceptor{
	
	private MemberService memberService;

	public loginCheckInterceptor(MemberService memberService) {
		super();
		this.memberService = memberService;
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		
		HttpSession session = request.getSession();
		String id = (String) session.getAttribute("id");
		
		if (id == null) {
			response.sendRedirect("/member/login");
			
			return false;
		}
		return true;
	}
}
