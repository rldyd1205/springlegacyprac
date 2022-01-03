package com.example.controller;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	public ResponseEntity<String> login(String id, String passwd, boolean rememberMe, HttpSession session,
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
	public ResponseEntity<String> join(MemberVO memberVO, String passwdConfirm) {
		// 1. 아이디 중복체크(DB에 있는지 확인)
		String id = memberVO.getId();

		MemberVO dbMemberVO = memberService.getMemberById(id);
		System.out.println("dbMemberVO : " + dbMemberVO);

		if (dbMemberVO != null) { // 이미존재하는 아이디
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "text/html; charset=UTF-8");

			String str = JScript.back("이미 존재하는 아이디입니다.");

			return new ResponseEntity<String>(str, headers, HttpStatus.OK);
		}
		// 2. 비밀번호, 비밀번호 확인 서로 같은지 체크
		String passwd = memberVO.getPasswd();

		// 비밀번호가 서로 다를때
		if (passwd.equals(passwdConfirm) == false) {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "text/html; charset=UTF-8");

			String str = JScript.back("비밀번호가 서로 다릅니다.");

			return new ResponseEntity<String>(str, headers, HttpStatus.OK);
		}

		// 아이디체크. 비밀번호체크 모두 통과했을때
		System.out.println("수정 전 memberVO : " + memberVO);
		// 회원가입 날짜 설정하기
		memberVO.setRegDate(new Date());

		// 비밀번호 암호화하기
		String hashPasswd = BCrypt.hashpw(passwd, BCrypt.gensalt());
		memberVO.setPasswd(hashPasswd);

		System.out.println("수정 후 memberVO : " + memberVO);

		// 3. DB에 등록
		memberService.insertMember(memberVO);

		// 4. 회원가입완료 메세지를 띄우고, 로그인화면으로 이동
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "text/html; charset=UTF-8");

		String str = JScript.href("회원가입 완료", "/member/login");

		return new ResponseEntity<String>(str, headers, HttpStatus.OK);
	} // join

	// 세션 쿠키 지우기(제거/삭제)
	@GetMapping("/logout")
	public String logout(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		// 세션 비우기
		session.invalidate();

		// 쿠키 지우기 -> 쿠키가 배열인 이유는 여러개의 쿠키가 있다.
		Cookie[] cookies = request.getCookies();
		// 쿠키가 빈값이 아닐때 if문 실행
		if (cookies != null) {
			// 반복문 forech사용(쿠키 하나씩 꺼내서 조건문을 통해 체크해봄)
			for (Cookie cookie : cookies) {
				// 쿠키 이름이 UserId(쿠키 봉다리)인 것만 꺼낼거임
				if (cookie.getName().equals("userId")) {
					// 쿠키 수명 0으로 만들기
					cookie.setMaxAge(0);
					// 쿠키 경로 설정 "/" 모든경로로
					cookie.setPath("/");
					// 쿠키 실어서 보내기(사용자에게 응답해주기)
					response.addCookie(cookie);
				}
			}
		}
		// member폴더 안에 있는 index.jsp(메인 페이지)로 보내기
		return "index";
	}

	/*
	 * /내정보에서 id를 받아와야 하는데 세션에 아이디가 등록되어 있으니까 session받아 오기.
	 */
	@GetMapping("/myInfo")
	public String myInfo(HttpSession session, Model model) {
		// 세션에 저장되어 있는 id 받아서 String id 변수에 저장
		String id = (String) session.getAttribute("id");
		// memberService에 저장되어 있는 id를 가져와서
		// memberVO에 넣어주기
		MemberVO memberVO = memberService.getMemberById(id);
		// 프론트로 던져 줄때 필요한게 모델 (세션과 비슷함)
		// member라는 이름으로 객체를 프론트에 던져줌
		// 즉 검색해서 나온 결과(memberVO)를 사용할 수 있음
		model.addAttribute("member", memberVO);
		// 내정보 페이지로 이동
		return "member/myInfo";
	}

	@GetMapping("/modify")
	public String ModifyForm(HttpSession session, Model model) {
		String id = (String) session.getAttribute("id");
		MemberVO memberVO = memberService.getMemberById(id);
		model.addAttribute("member", memberVO);

		return "member/memberModify";
	}

	@PostMapping("/modify")
	public ResponseEntity<String> modify(MemberVO memberVO, HttpSession session) {
		String id = (String) session.getAttribute("id");

		// memberVO가 잘 조립해서 가지고 왔는지 출력문 작성
		System.out.println("memberVO : " + memberVO);
		// disabled로 막혀있어서 아이디를 memberVO가 못가져오기 때문에,
		// setId로 id를 넣어주기
		memberVO.setId(id);
		System.out.println("수정 후 memberVO : " + memberVO);

		// 1. 비밀번호 맞는지 체크해서 틀리면 다시 돌려 보내기
		MemberVO dbMemberVO = memberService.getMemberById(id);
		// memberVO(사용자가 입력한 비밀번호).getPasswd() = 암호화가 되기 전 비밀번호
		// dbMemberVO(DB에 암호화 되서 저장되있는 비밀번호).getPasswd() = 암호화가 되고난 후 비밀번호

		// checkpw여기 안에다가 넣으면 비교를 해줌 비밀번호를 암호화
		// 해서 DB에 저장되 있는 암호화 된 비밀번호랑 같은지 맞으면 true 틀리면 false
		boolean isPasswdRight = BCrypt.checkpw(memberVO.getPasswd(), dbMemberVO.getPasswd());

		if (isPasswdRight == false) { // 비밀번호 틀림
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "text/html; charset=UTF-8");
			String str = JScript.back("비밀번호가 틀렸습니다.");

			return new ResponseEntity<String>(str, headers, HttpStatus.OK);
		}

		// 2. 비밀번호가 true일때 DB정보 수정하기 -> DB정보 수정하는 Mapper만들어 줘야 함
		// 수정 완료
		memberService.modifyMember(memberVO);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "text/html; charset=UTF-8");
		String str = JScript.href("회원정보 수정 완료", "/member/myInfo");

		return new ResponseEntity<String>(str, headers, HttpStatus.OK);
	}

	@GetMapping("/passwd")
	public String passwdForm() {

		return "member/passwd";
	}

	@PostMapping("/passwd")
	public ResponseEntity<String> passwd(String passwd, String newPasswd, String newPasswdConfirm,
			HttpSession session) {
		// 1. 현재 비밀번호 맞는지 체크
		String id = (String) session.getAttribute("id");
		MemberVO dbMemberVO = memberService.getMemberById(id);

		boolean isPasswdRight = BCrypt.checkpw(passwd, dbMemberVO.getPasswd());

		if (isPasswdRight == false) { // 현재 비밀번호가 맞지 않음
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "text/html; charset=UTF-8");
			String str = JScript.back("현재 비밀번호가 틀렸습니다.");

			return new ResponseEntity<String>(str, headers, HttpStatus.OK);
		}
		// 2. 새 비밀번호 , 새비밀번호 확인 맞는지 체크
		if (newPasswd.equals(newPasswdConfirm) == false) {// 새 비밀번호와 새비밀번호 확인이 서로 다를 때
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "text/html; charset=UTF-8");
			String str = JScript.back("새 비밀번호와 새 비밀번호 확인이 서로 일치하지 않습니다.");

			return new ResponseEntity<String>(str, headers, HttpStatus.OK);
		}
		// 3. DB비밀번호 변경-> 비밀번호 바꿔주는 Mapper 가서 만들기

		// 3-1 비밀번호 암호화
		String hashPasswd = BCrypt.hashpw(newPasswd, BCrypt.gensalt());

		// 세션에서 가져온id랑 사용자가 비밀번호 변경 페이지에서 입력한newPasswd를 modifyPasswd넣어줌
		memberService.modifyPasswd(id, hashPasswd);

		// 4. 비밀번호 완료 메세지 띄우고 로그아웃처리
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "text/html; charset=UTF-8");
		// 비밀번호를 변경하고 로그아웃 시키는 곳으로 이동
		// -> 그러면 세션도 삭제하고 쿠키 수명도 없애고 난 이후에 메인페이지인 index로 이동
		String str = JScript.href("비밀번호 변경 완료", "/member/logout");

		return new ResponseEntity<String>(str, headers, HttpStatus.OK);
	}

	// 회원 탈퇴 페이지
	@GetMapping("/remove")
	public String removeForm() {

		return "member/memberRemove";
	}

	@PostMapping("/remove")
	public ResponseEntity<String> removrForm(String passwd, HttpSession session) {
		// 1. 비밀번호 체크
		String id = (String) session.getAttribute("id");
		MemberVO dbMemberVO = memberService.getMemberById(id);

		boolean isPasswdRight = BCrypt.checkpw(passwd, dbMemberVO.getPasswd());

		if (isPasswdRight == false) { // 현재 비밀번호가 맞지 않음
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "text/html; charset=UTF-8");
			String str = JScript.back("비밀번호가 틀렸습니다.");

			return new ResponseEntity<String>(str, headers, HttpStatus.OK);
		}
		// 2. DB에서 해당 아이디 정보 삭제
		memberService.deleteMemberById(id);

		// 3. 로그아웃 처리(세션, 쿠키 삭제)
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "text/html; charset=UTF-8");
		// 비밀번호를 변경하고 로그아웃 시키는 곳으로 이동
		// -> 그러면 세션도 삭제하고 쿠키 수명도 없애고 난 이후에 메인페이지인 index로 이동
		String str = JScript.href("회원탈퇴 완료", "/member/logout");

		return new ResponseEntity<String>(str, headers, HttpStatus.OK);
	}
}
