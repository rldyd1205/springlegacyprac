package com.example.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

public class RememberMeInterceptor implements HandlerInterceptor{
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		
		HttpSession session = request.getSession();
		String id = (String) session.getAttribute("id");
		
		if (id == null) {
			Cookie[] cookies = request.getCookies();
			
			if (cookies == null) {
				for (Cookie cookie : cookies) {
					if (cookie.getName().equals("UserId")) {
						id = cookie.getValue();
					}
				}
			}
		}
		
		return true;
	}
	
}
