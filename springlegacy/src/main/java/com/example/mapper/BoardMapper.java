package com.example.mapper;

import com.example.domain.BoradVO;

public interface BoardMapper {

	// =========== select ===========
	int getNextNum();
	// =========== insert ===========
	void writeBoard(BoradVO boardVO);
	// =========== update ===========
	
	// =========== delete ===========
	
}
