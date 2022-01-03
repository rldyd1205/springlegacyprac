package com.example.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

// 로그인 유지
public class RememberMeInterceptor implements HandlerInterceptor{
	
	// 세션이 없을 때 쿠키를 뒤져봐서 있으면 그걸로 로그인
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		
		// 세션이 있는지 확인
		HttpSession session = request.getSession();
		String id = (String) session.getAttribute("id");
		
		// 세션 없으면 쿠키확인
		if (id == null) {
			Cookie[] cookies = request.getCookies();
			
			if (cookies == null) {
				for (Cookie cookie : cookies) {
					if (cookie.getName().equals("UserId")) {
						id = cookie.getValue();
						
						session.setAttribute("id", id);
					}
				}// for
			} 
		}// if
		
		return true;
	}
	
}
