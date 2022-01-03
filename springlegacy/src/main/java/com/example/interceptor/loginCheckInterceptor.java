package com.example.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

import com.example.domain.MemberVO;
import com.example.service.MemberService;

public class loginCheckInterceptor implements HandlerInterceptor {

	private MemberService memberService;

	public loginCheckInterceptor(MemberService memberService) {
		super();
		this.memberService = memberService;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws IOException {

		HttpSession session = request.getSession();
		String id = (String) session.getAttribute("id");

		if (id == null) {
			response.sendRedirect("/member/login");

			return false; // 예정이었던 컨트롤러는 호출 하지 말아야함. false
		}

		MemberVO memberVO = memberService.getMemberById(id);
		System.out.println(memberVO.toString());

		return true;
	}
}
