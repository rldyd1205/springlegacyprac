package com.example.domain;

import lombok.Data;

@Data
public class Criteria {
	
	private int pageNum; 		 // 페이지 번호
	private int amount;			 // 한 페이지당 글 개수
	
	private int startRow;		 // 가져올 글의 시작 행번호 
	
	private String type    = ""; // 검색 유형 (제목)
	private String keyword = ""; // 검색어 (작성자/글쓴이)
	
	public Criteria() {
		this(1, 10);
	}
	
	// 아무런 값을 쥐어 주지 않으면 
	// pageNum은 1, amount는 10의 값을 기본으로 줌
	// 즉 페이지 번호는 1번이 되고, 1번페이지에 글 개수는 10개
	// DB에서 가장 최신의 적은 글이 상단의 위치하게끔 DESC를 사용함
	public Criteria(int pageNum, int amount) {
		super();
		this.pageNum = pageNum;
		this.amount  = amount;
	}
	
	
}
