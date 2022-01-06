package com.example.domain;

import lombok.Data;

@Data
public class AttachVO {
	
	private String uuid;		// 고유 id -랜덤의로 아이디 만들어 줌
	private String uploadpath;  // 업로드 경로
	private String filename;    // 파일 이름
	private String filetype;	// I - 이미지 파일 // O - 기타 파일
	private int    boardNum;	// 어느게시글의 첨부파일인지
	
}
