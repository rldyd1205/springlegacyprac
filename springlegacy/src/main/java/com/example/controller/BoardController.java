package com.example.controller;

import java.nio.file.Files;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.domain.AttachVO;
import com.example.domain.BoardVO;
import com.example.domain.Criteria;
import com.example.domain.PageDTO;
import com.example.service.BoardService;

@Controller
@RequestMapping("/board/*")
public class BoardController {

	private BoardService boardService;

	public BoardController(BoardService boardService) {
		super();
		this.boardService = boardService;
	}

	// 게시글 목록 페이지 가져오기
	@GetMapping("/list")
	public String boardList(Criteria cri, Model model) {

		System.out.println("cri : " + cri);

		List<BoardVO> boardList = boardService.getBoardsByCri(cri);

		int totalCount = boardService.getCountBoardsByCri(cri);

		System.out.println("totalCount : " + totalCount);

		PageDTO pageDTO = new PageDTO(totalCount, cri);

		model.addAttribute("boardList", boardList);
		model.addAttribute("pageMaker", pageDTO);

		return "board/boardList";
	}

	// 새글 쓰기 페이지 가져오기
	@GetMapping("/write")
	public String writeForm() {

		return "board/writeBoard";
	}

	// 새글 쓰기 버튼 눌렀을 때 기능 구현
	// 기능 다 작동하고 나서 return으로 board/boardContent페이지 창 띠어주기
	// board/boardContent -> 게시글 상세보기
	@PostMapping("write")
	public String write(BoardVO boardVO, HttpSession session, RedirectAttributes rttr) {
		System.out.println("수정 전 BoardVO : " + boardVO);

		// 1. 새 글 번호 추가
		int nextNum = boardService.getNextNum();
		System.out.println("nextNum : " + nextNum);
		boardVO.setNum(nextNum);
		
		// 2. 글쓴이 추가
		String id = (String) session.getAttribute("id");
		boardVO.setMemberId(id);

		// 3. 조회수 0
		boardVO.setViewCount(0);

		// 4. 글 쓴 날짜 추가(글쓴 그시간에 시간이 적힘)
		boardVO.setRegDate(new Date());

		// 5. reRef, reLev, reSeq 설정
		boardVO.setReRef(nextNum);
		boardVO.setReLev(0);
		boardVO.setReSeq(0);

		System.out.println("수정 후 BoardVO : " + boardVO);

		// DB에 등록하기
		boardService.writeBoard(boardVO);

		// 글 번호로 다음 페이지 다음 요청으로 이동
		// 리다이렉트로 글 번호 전달하기
		rttr.addAttribute("num", boardVO.getNum());
		// 게시글 목록으로 돌아갈 때 게시글 리스트가 있으면 거기서
		// 예를 들어 5페이지를 눌러서 상세보기들어왔다가
		// 목록으로 돌아갈때 다시 5페이지로 돌아가야 해서 필요하다
		rttr.addAttribute("pageNum", 1);

		// board/writeBoard.jsp -> board/boardContent.jsp 이동
		return "redirect:/board/content";
	}

	// 게시글 상세보기페이지 가져오기
	@GetMapping("/content")
	public String boardContent(int num,
			// pageNum은 필수로 요구되는 사항이 아니고, 까먹고 pageNum을 안보내줬으면
			// 기본값 1로 설정해서 보내준다
			@RequestParam(required = false, defaultValue = "1") String pageNum, Model model) {

		// num, pageNum 값 잘 가져왔는지 체크
		System.out.println("num : " + num);
		System.out.println("pasgeNum : " + pageNum);

		// 조회수 1 증가
		// 조회수 증가를 먼저 시킨다음에 DB에서 글정보를 가지고 와야함
		// 조회수 증가하는게 밑에 있으면 DB에서는 조회수 카운트가 올라가는데
		// 화면에서는 조회수가 증가하지 않음
		boardService.addViewCount(num);

		// num에 해당하는 글 정보 DB에서 가져오기
		BoardVO boardVO = boardService.getBoardByNum(num);
		System.out.println("boardVO : " + boardVO);

		model.addAttribute("board", boardVO);
		model.addAttribute("pageNum", pageNum);

		return "board/boardContent";
	}
}
