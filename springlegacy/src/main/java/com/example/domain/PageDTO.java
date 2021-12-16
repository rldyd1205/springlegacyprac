package com.example.domain;

import lombok.Data;

@Data
public class PageDTO {
	
	private int 	  startPage;	// 페이징 블럭 안에서의 시작페이지
	private int 	  endPage;		// 페이징 블럭 안에서의 끝 페이지
	private boolean   prev;			// 이전 페이지 유무
	private boolean   next;			// 다음 페이지 유무
	
	// 페이지블록 구성하는 최대 페이지 개수
	// 우리는 5로 지정을 해뒀기 때문에
	// < 1 2 3 4 5 > / < 6 7 8 9 10 > 이런 모양으로 만들어 진다.
	private final int PAGE_BLOCK = 5; 
	
	private int 	  totalCount;	// 전체 글 개수
	private Criteria  cri;			// 요청 페이지번호, 한페이지당 글 개수
	
	// Criteria에서 페이지 번호랑 한 페이지당 글개수를 가져온다.
	// totalCount는 DB에 저장되어 있는 글 목록을 가지고 온다.
	public PageDTO(int totalCount, Criteria cri) {
		super();
		this.totalCount = totalCount;
		this.cri = cri;
		
		// 1~5 페이지 중의 어느 번호를 요청해도 항상 끝페이지는 5
		// 6~10 페이지 요청시 끝페이지는 10으로 바뀜
		// getPageNum 이 3 이라고 할때. 5로 실수나눗셈 0.6 올림(ceil) -> 1 곱하기 5 -> endPage 5
		endPage = (int) Math.ceil((double) cri.getPageNum() / PAGE_BLOCK) * PAGE_BLOCK;
		
		// endPage가 예를 들어 4면? 
		// startPage = 4 - (5-1)
		// startPage에는 1이 대입된다 즉 첫 시작페이지는 1번이라는 말이다.
		startPage = endPage - (PAGE_BLOCK - 1);
		
		// 실제 페이지가 PAGE_BLOCK만큼 딱 안떨어질 때
		// 예를 들어 totalCount=38이고 getAmount=10일때 38/10을 하면 3.8이 나온다
		// 3.8을 올림해서 4가되고 그걸 realEnd에 넣어준다.
		int realEnd = (int) Math.ceil((double) totalCount / cri.getAmount());
		
		// realEnd가 4이고, endPage가 5일때
		// 조건 성립해서(true) 마지막 페이지는 우리가 지정해놓은 
		// PAGE_BLOCK 5가 아닌 4에서 끝이 난다.
		if (realEnd < endPage) {
			endPage = realEnd;
		}
		
		// 왼쪽 페이지로 이동하는 버튼 같은 거
		// startPage 즉 내가 1번 페이지를 보고 있으면
		// < 왼쪽으로 이동할수 있는 버튼은 안만들어 줌
		// 1 2 3 4 >
		prev = startPage > 1;
		// 오른쪽 페이지로 이동하는 버튼 같은 거
		// endPage 즉 내가 4페이지 를 보고 있다.
		// 그러면 마지막 페이지와 보고있는 페이지가 같이때문에
		// > 오른쪽으로 이동할 수 있는 버튼은 안만들어줌
		// < 1 2 3 4
		next = endPage < realEnd;
	} // 생성자
	
	
}
