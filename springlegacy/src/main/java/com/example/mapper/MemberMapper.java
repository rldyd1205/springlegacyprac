package com.example.mapper;

import org.apache.ibatis.annotations.Param;

import com.example.domain.MemberVO;

public interface MemberMapper {
	
	// =========== select ===========
	MemberVO getMemberById(String id);
	
	// =========== insert ===========
	void insertMember(MemberVO memberVO);
	
	// =========== update ===========
	// DB정보 수정하는 Mapper 만들기 
	// -> 무슨 내용인지 명세하기 위해 MemberMapper.xml에 가서 
	// 어떤 내용인지 적어 줘야 함
	void modifyMember(MemberVO memberVO);
	
	// 두개를 적을 경우 앞에@Param 적어줘야 함
	void modifyPasswd(@Param("id") String id, 
					  @Param("newPasswd") String newPasswd);
	
	// =========== delete ===========
	
}
