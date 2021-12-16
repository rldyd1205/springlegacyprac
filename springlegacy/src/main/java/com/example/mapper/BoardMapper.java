package com.example.mapper;

import java.util.List;

import com.example.domain.BoardVO;

public interface BoardMapper {

	// =========== select ===========
	int getNextNum();
	
	BoardVO getBoardByNum(int num);

	// 임시
	List<BoardVO> getAllBoards();
	
	// =========== insert ===========
	void writeBoard(BoardVO boardVO);
	
	
	// =========== update ===========
	
	// 조회수를 +1 해주는 거 만드는거
	void addViewCount(int num);
	
	// =========== delete ===========
	
}
