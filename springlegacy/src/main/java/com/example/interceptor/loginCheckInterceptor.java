package com.example.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

import com.example.domain.MemberVO;
import com.example.service.MemberService;

// 로그인 체크 -> 로그인이 필요한 서비스에서 로그인 안하고 접근했을 경우를 대비
public class loginCheckInterceptor implements HandlerInterceptor {

	private MemberService memberService;

	public loginCheckInterceptor(MemberService memberService) {
		super();
		this.memberService = memberService;
	}

	// 사용자에게 요청받을걸 컨트롤러에 가기전에 막는데 그걸 이름을 preHandler라고 해서
	// 메서드 이름을 preHandle이라고 정함
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws IOException {

		HttpSession session = request.getSession();
		String id = (String) session.getAttribute("id");

		// 세션에 아이디가 없을때
		if (id == null) {

			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html; charset=UTF-8");

			response.getWriter().print("<script>alert('로그인이 필요한 서비스입니다'); location.href = '/member/login';</script>");

			return false; // 예정이었던 컨트롤러는 호출 하지 말아야함. false
		}

		MemberVO memberVO = memberService.getMemberById(id);
		System.out.println(memberVO.toString());

		// 있으면 사용자가 요청한 곳으로 바로 이동
		return true;
	}
}
