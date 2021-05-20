package com.kh.board.service;

import static com.kh.common.JDBCTemplate.getConnection; // 직접 만들어줘야함 -> JDBCTemplate안에 'public static Connection getConnection() {}' 있어야 가능
import static com.kh.common.JDBCTemplate.commit; // 직접 만들어줘야함 -> JDBCTemplate안에 'public static void commit(Connection conn) {}' 있어야 가능
import static com.kh.common.JDBCTemplate.rollback; // 직접 만들어줘야함 -> JDBCTemplate안에  'public static void rollback(Connection conn) {}' 있어야 가능

import java.sql.Connection;
import java.util.ArrayList;

import com.kh.board.model.dao.BoardDAO;
import com.kh.board.model.vo.Board;

public class BoardService { // 본격적으로 자바와 sql 연결

	public ArrayList<Board> selectAll() { // 2. 글 목록 조회
		Connection conn = getConnection(); // 자바와 sql을 연결하는 실질적인 객체
		BoardDAO bDAO = new BoardDAO(); // Board 객체를 만들고 sql에서 검색한 값들을 객체에 대입한 후 대입해서 만든 Arraylist<Board>를 반환(검색 결과를 반환한다는 뜻) 
		ArrayList<Board> list = bDAO.selectAll(conn); // 연결 값을 list에 저장
		
		return list;
	}

	public Board selectOne(int no) { // 3. 게시물 상세 조회
		Connection conn = getConnection();
		BoardDAO bDAO = new BoardDAO();
		Board board = bDAO.selectOne(conn, no);
		return board;
	}
	
	public int insertBoard(Board board) { // 4. 글 쓰기
		Connection conn = getConnection();
		BoardDAO bDAO = new BoardDAO();
		int result = bDAO.insertBoard(conn, board); // 메소드의 반환형을 반환값으로 삼음
		
		if(result > 0) {
			commit(conn);
		} else {
			rollback(conn);
		}
		
		
		
		return result;
	}

	public int updateBoard(int no, String selStr, String upStr) { // 5. 글 수정
		Connection conn = getConnection(); // 서비스에 들어왔으면 젤 먼저 커넥션 연결
		BoardDAO bDAO = new BoardDAO();
		int result = bDAO.updateBoard(conn, no, selStr, upStr);
		
		if(result > 0) {
			commit(conn);
		} else {
			rollback(conn);
		}
		
		return result;
	}

	public int deleteBoard(int no) { // 6. 글 삭제
		Connection conn = getConnection();
		BoardDAO bDAO = new BoardDAO();
		int result = bDAO.deleteBoard(conn, no);
		
		if(result > 0) { // int 반환일 때 하나봐...
			commit(conn);
		} else {
			rollback(conn);
		}
		
		return result;
	}

	

}
