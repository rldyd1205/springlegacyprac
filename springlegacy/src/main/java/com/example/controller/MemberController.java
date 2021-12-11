package com.example.controller;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.MemberVO;
import com.example.service.MemberService;
import com.example.util.JScript;

@Controller
@RequestMapping("/member/*")
public class MemberController {
	
	private MemberService memberService;

	public MemberController(MemberService memberService) {
		super();
		this.memberService = memberService;
	}

	@GetMapping("/login")
	public String loginForm() {
		System.out.println("loginForm() 호출됨...");
		return "member/login";
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(String id, String passwd, 
			boolean rememberMe, 
			HttpSession session, 
			HttpServletResponse response) {
		// 1. 아이디 존재 여부 체크
		MemberVO dbMemberVO = memberService.getMemberById(id);

		if (dbMemberVO == null) { // 아이디가 존재하지 않을 경우
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "text/html; charset=UTF-8");
			String str = JScript.back("존재하지 않는 아이디 입니다.");

			return new ResponseEntity<String>(str, headers, HttpStatus.OK);
		}
		// 2. 비밀번호 체크
		String realHashPasswd = dbMemberVO.getPasswd();

		Boolean isRightPasswd = BCrypt.checkpw(passwd, realHashPasswd);
		if (isRightPasswd == false) { // 비밀번호가 다름
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "text/html; charset=UTF-8");

			String str = JScript.back("비밀번호가 틀렸습니다.");

			return new ResponseEntity<String>(str, headers, HttpStatus.OK);
		}
		System.out.println("realHashPasswd : " + realHashPasswd);
		// 3. 세션 등록
		session.setAttribute("id", id);
		// 로그인상태유지
		if (rememberMe == true) { // true상태일때 로그인 유지 체크 함
			// 쿠키 등록하기
			Cookie cookie = new Cookie("userId", id); // 쿠키생성
			// 쿠키수명 설정 초 * 분 * 시 * 일
			cookie.setMaxAge(60 * 60 * 24 * 7);
			// 쿠키 적용 경로 설정 여기서 "/"는 webapp이다 즉 모든 경로이다
			cookie.setPath("/");
			// 사용자에게 보낼때 쿠키를 적용시켜서 보내준다.
			response.addCookie(cookie);
		}

		// 5. 메인화면을 보내기
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "text/html; charset=UTF-8");

		String str = JScript.href("로그인 완료", "/");

		return new ResponseEntity<String>(str, headers, HttpStatus.OK);
	}

	@GetMapping("/join")
	public String joinForm() {
		System.out.println("joinForm() 호출됨...");
		return "member/join";
	}

	@PostMapping("/join")
	public ResponseEntity<String> join(MemberVO memberVO, String passwdCornfirm) {
		// 1.아이디 중복 체크
		String id = memberVO.getId();

		MemberVO dbmemberVO = memberService.getMemberById(id);

		if (dbmemberVO != null) {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "text/html; charset=UTF-8");
			String str = JScript.back("중복된 아이디가 있습니다.");

			return new ResponseEntity<String>(str, headers, HttpStatus.OK);
		}
		
		// 2. 비밀번호 맞는지 체크
		String passwd = memberVO.getPasswd();

		if (passwd.equals(passwdCornfirm)) {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "text/html; charset=UTF-8");
			String str = JScript.back("비밀번호가 틀렸습니다.");

			return new ResponseEntity<String>(str, headers, HttpStatus.OK);
		}
		
		// 3. 회원가입 날짜
		memberVO.setRegDate(new Date());
		
		// 4. 비밀번호 암호화
		String hasPasswd = BCrypt.hashpw(passwd, BCrypt.gensalt());
		memberVO.setPasswd(hasPasswd);
		
		// 5. DB에 회원정보 등록하기(가입하기)
		System.out.println("memberVO : " + memberVO);
		memberService.insertMember(memberVO);
		
		// 6. 회원가입 완료 메세지 + 로그인 페이지로 보내기
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "text/html; charset=UTF-8");
		String str = JScript.href("회원가입 완료", "/member/login");

		return new ResponseEntity<String>(str, headers, HttpStatus.OK);
	}

}
