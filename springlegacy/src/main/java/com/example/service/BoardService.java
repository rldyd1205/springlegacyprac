package com.example.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.BoardVO;
import com.example.mapper.BoardMapper;

@Service
@Transactional
public class BoardService {
	
	private BoardMapper boardMapper;

	public BoardService(BoardMapper boardMapper) {
		super();
		this.boardMapper = boardMapper;
	}
	
	public int getNextNum() {
		return boardMapper.getNextNum();
	}
	
	public void writeBoard(BoardVO boardVo) {
		boardMapper.writeBoard(boardVo);
	}
	
	public BoardVO getBoardByNum(int num) {
		return boardMapper.getBoardByNum(num);
	}
	
	public void addViewCount(int num) {
		boardMapper.addViewCount(num);
	}
}
