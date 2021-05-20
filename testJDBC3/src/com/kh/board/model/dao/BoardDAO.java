package com.kh.board.model.dao;

import static com.kh.common.JDBCTemplate.close;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import com.kh.board.model.vo.Board;

public class BoardDAO { // sql에서 값을 받아옴
	
	private Properties prop = null;
	
	public BoardDAO() { // query.properties를 불러오기 위해 쓰는 건가....?
		try {
			prop = new Properties();
			prop.load(new FileReader("query.properties")); // 원래는 board property, member property 따로 만드는 게 맞지만 일단은 같이
			// Property 객체에 query.properties를 읽어 불러옴 => prop에 query.properties가 있으므로 'prop = query.properties'
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public ArrayList<Board> selectAll(Connection conn) { // 2. 글 목록 조회
		Statement stmt = null; // preparedStatement는 x -> 모두 검색하니까 조건이 필요 없으므로
		ResultSet rset = null; // select문이므로 필요
		ArrayList<Board> list = new ArrayList<Board>(); // 컬렉션으로 불러옴 => 값이 없는 상태임
		
		String query = prop.getProperty("selectAll"); // prop(query.properties)안에 있는 selectAll를 불러와 쿼리로 쓰겠다
		
		try {
			stmt = conn.createStatement(); // 쿼리를 이클립스에서 sql로 보내는 역할인 statement를 만들어줌
			rset = stmt.executeQuery(query); // 검색 결과를 나타내는 resultset을 만들어줌. (query)는 query에 대한 검색을 하라는 의미
			
			// 검색을 수행
			
			while(rset.next()) { // 검색값이 다음에도 있으면
				Board board = new Board(rset.getInt("BNO"), 
										rset.getString("TITLE"),
										rset.getString("CONTENT"),
										rset.getDate("CREATE_DATE"),
										rset.getString("WRITER"));
				// 객체를 만들고 대입하는 것이 아니라 객체 생성과 동시에 대입
				// board라는 객체에(한 줄씩이므로 list 아니고 그냥 객체임 board객체가 여러개일 때 list) 
				// sql에 있는 BNO(int), TITLE(String), CONTENT(String), CREATE_DATE(Date), WRITER(String)을 객체에 대입해라
				
				
				/*
				 * 따로따로 하고 싶으면  
				 
				 int bNo = rset.getInt("BNO")
				 String title = rset.getString("TITLE")
				 ...
				 
				 Board board = new Board(bNo, title, ...);
				 */

				list.add(board); // list에 board라는 객체를 추가함! 짱 중요
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rset);
			close(stmt);
		}

		return list; // 값을 대입한 객체들을 컬렉션 형태로 만들어(list) 목록들을 반환
	}

	
	public Board selectOne(Connection conn, int no) { // 3. 게시물 상세 조회
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Board board = null;
		
		String query = prop.getProperty("selectOne");
		// selectOne=SELECT * FROM BOARD WHERE BNO = ? AND DELETE_YN = 'N'

		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, no);
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				board = new Board(rset.getInt("BNO"),
						rset.getString("TITLE"),
						rset.getString("CONTENT"),
						rset.getDate("CREATE_DATE"),
						rset.getString("WRITER"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rset);
			close(pstmt);
		}
		
		
		return board;
	}
	
	
	public int insertBoard(Connection conn, Board board) { // 4. 글 쓰기
		PreparedStatement pstmt = null;
		int result = 0;
		
		String query = prop.getProperty("insertBoard");
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, board.getTitle());
			pstmt.setString(2, board.getContent());
			pstmt.setString(3, board.getWriter());
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}

		return result;
	}

	public int updateBoard(Connection conn, int no, String selStr, String upStr) { // 5. 글 수정
		PreparedStatement pstmt = null;
		int result = 0;
		
		String query = prop.getProperty("update" + selStr);
		// updateTitle=UPDATE BOARD SET TITLE = ?WHERE BNO = ?
		// updateContent=UPDATE BOARD SET CONTENT = ? WHERE BNO = ?
		/* 주의사항
		    updateBoard=UPDATE BOARD SET ? = ? WHERE BNO = ?
		         위치홀더는 ''를 포함하기 때문에 컬럼값에 들어갈 수 없음   */
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, upStr);
			pstmt.setInt(2, no);
			
			result = pstmt.executeUpdate(); // 값을 채웠으면 executeUpdate()을 통해 값을 보내줌
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	public int deleteBoard(Connection conn, int no) { // 6. 글 삭제
		PreparedStatement pstmt = null;
		int result = 0;
		
		String query = prop.getProperty("deleteBoard");
		// deleteBoard=UPDATE BOARD SET DELETE_YN = 'Y' WHERE BNO = ?
		// UPDATE인 이유는 회원탈퇴를 해도 기업에서 일정기간 정보를 가지고 있어야하기 때문에
		/* selectOne에서 selectOne=SELECT * FROM BOARD WHERE BNO = ? AND DELETE_YN = 'N'로 
		 	selectAll에서 selectAll=SELECT * FROM BOARD WHERE DELETE_YN = 'N' 로
		 	둘 다 DELETE_YN = 'N' 인 것만 보이게 설정해놨기 때문에 DELETE_YN = 'Y'로 변경하면 글이 보이지 않게 되는 원리 */
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, no);
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	

}
