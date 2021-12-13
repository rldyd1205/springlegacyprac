package com.example.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.MemberVO;
import com.example.mapper.MemberMapper;

//MemberMapper는 interface라서 private로 가져 와야 한다.
//즉 MemberMapper와 MemberService스프링에게 말해서 연결시키는 코드

//그래서 사용하려면 생성자가 필요함

@Service
@Transactional
public class MemberService {
	
	private MemberMapper memberMapper;
	
	public MemberService(MemberMapper memberMapper) {
		super();
		this.memberMapper = memberMapper;
	}

	public MemberVO getMemberById(String id) {
		return memberMapper.getMemberById(id);
	}
	
	public void insertMember(MemberVO memberVO) {
		memberMapper.insertMember(memberVO);
	}
}
