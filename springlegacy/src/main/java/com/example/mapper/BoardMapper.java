package com.example.mapper;

import com.example.domain.BoardVO;

public interface BoardMapper {

	// =========== select ===========
	int getNextNum();
	
	BoardVO getBoardByNum(int num);
	
	// =========== insert ===========
	void writeBoard(BoardVO boardVO);
	
	
	// =========== update ===========
	
	// 조회수를 +1 해주는 거 만드는거
	void addViewCount(int num);
	
	// =========== delete ===========
	
}
