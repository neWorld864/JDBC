package com.kh.member.model.service;

import static com.kh.common.JDBCTemplate.getConnection; // 직접 만들어줘야함 -> JDBCTemplate안에 'public static Connection getConnection() {}' 있어야 가능
import static com.kh.common.JDBCTemplate.close; // 직접 만들어줘야함 -> JDBCTemplate안에 'public static void close(Connection conn) {}' 있어야 가능

import java.sql.Connection;

import com.kh.member.model.dao.MemberDAO;
import com.kh.member.model.vo.Member;

public class MemberService {

	public int login(Member mem) { // 1. 로그인
		Connection conn = getConnection();
		MemberDAO mDAO = new MemberDAO();
		
		int result = mDAO.login(conn, mem); // conn과 mem을 mDAO에 있는 login메소드에 넘겨줌
		
		return result;
	}

	public void exitProgram() { // 0. 프로그램 종료
		close(getConnection());
	}


	
}
