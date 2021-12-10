package com.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.MemberVO;
import com.example.service.MemberService;

@Controller
@RequestMapping("/member/*")
public class MemberController {
	
	@GetMapping("/login")
	public String loginForm() {
		System.out.println("loginForm() 호출됨...");
		return "member/login";
	}
	
	@GetMapping("/join")
	public String joinForm() {
		System.out.println("joinForm() 호출됨...");
		return "member/join";
	}
	
	@PostMapping("/join")
	public ResponseEntity<String> join(MemberVO memberVO) {
		// 1. id 중복체크
		String id = memberVO.getId();
		
		MemberVO dbmemberVo = MemberService.getMemberById(id);
		// 2. 비밀번호 확인 맞는지 체크
		// 3. DB에 회원정보 등록하기(가입하기)
		// 4. 회원가입 완료 메세지 + 로그인 페이지로 보내기
		return null;
	}
}
