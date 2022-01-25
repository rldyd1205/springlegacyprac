package com.example.domain;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class BoardVO {
	private int 	num;		// 게시글 번호
	private String 	memberId;	// 글쓴이
	private String 	subject;	// 제목
	private String 	content;	// 내용
	private int 	viewCount;	// 조회수
	private Date 	regDate;	// 등록일
	private int 	reRef;		// 답글 쓸 때 필요함(참조)
	private int 	reLev;		// 답글 쓸 때 필요함
	private int 	reSeq;		// 답글 쓸 때 필요함
	
	private List<AttachVO> attachList;
}
