package com.kh.model.dao;

import static com.kh.common.JDBCTemplate.close; // close는 static이므로 static을 붙여줌

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import com.kh.model.vo.Member;

public class MemberDAO {
	
	private Properties prop = null;
	
	public MemberDAO() {
		
		try { // multi catch(x) 그냥 try catch
			prop = new Properties();
			prop.load(new FileReader("query.properties")); // filereader -> 읽어올 file 생성(query.properties)
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	public int insertMember(Connection conn, Member mem) { // 매개변수 conn과 mem을 MemberService에서 받아옴
		/*
		 	이전 프로젝트에서 DAO가 맡은 업무
		 		1) JDBC 드라이버 등록
		 		2) DB 연결 Connection객체 생성
		 		3) SQL 실행
		 		4) 처리 결과에 따른 트랜잭션 처리(commit, rollback)
		 		5) 자원 반환
		 		
		 	실제로 DAO가 해야하는 업무는 3번만 해야 함
		 	--> 1, 2, 4, 5 역할 분리
		 	--> 어디서든지 일어나는 공통 업무이기 때문에 한 번에 묶어서 처리 : JDBCTemplate클래스 구현
		 */
		
		PreparedStatement pstmt = null;
		int result = 0;
		
		String query = prop.getProperty("insertMember"); // ?로 써주는 거 귀찮으니까 따로 properties 만들어서 거기에 '?' 만들어주고 여기에 그냥 가져오기만 하면 됨
		
		
		try {
			pstmt = conn.prepareStatement(query);
			// setString(String str) 
			pstmt.setString(1, mem.getMemberId());
			pstmt.setString(2, mem.getMemberPwd());
			pstmt.setString(3, mem.getMemberName());
			pstmt.setString(4, mem.getGender()+""); // +"" 붙이는게 char->string 형변환
			pstmt.setString(5, mem.getEmail());
			pstmt.setString(6, mem.getPhone());
			pstmt.setString(7, mem.getAddress());
			pstmt.setInt(8, mem.getAge());
			
			
			// INSERT할 때 sql 테이블 순서에 맞게 넣어주어야 한다.!
			
			result = pstmt.executeUpdate();
			
			// commit, rollback 하는 건 다른 곳에서 함~
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
//			JDBCTemplate.close(pstmt);
			close(pstmt); // import에서 살짝 변경해줘야함
		}

		return result;
	}
	
	public ArrayList<Member> selectMemberId(Connection conn, String id) {
//		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		ArrayList<Member> list = new ArrayList<Member>();
		
		String query = prop.getProperty("selectMemberId");
		// SELECT * FROM MEMBER WHERE MEMBER_ID LIKE '%id%'
		// SELECT * FROM MEMBER WHERE MEMBER_ID LIKE 
//		query += "'%" + id + "%'";
		
		
		try {
//			stmt = conn.createStatement();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, "%"+id+"%"); // ' 빼고 넣음 : PreparedStatement 위치홀더를 이용하여 값을 넣으면 자동으로 ' ' 가 붙음
//			rset = stmt.executeQuery(query);
			rset = pstmt.executeQuery();
			
			while(rset.next()) { // SQL에 순서가 ADDRESS-AGE 순인데 AGE-ADDRESS 순으로 써도 상관 없는 건가요?? 인덱스 쓰는 경우만 순서 지키면 되는 건가용 
				String memberId = rset.getString("MEMBER_ID");
				String memberPwd = rset.getString("MEMBER_PWD");
				String memberName = rset.getString("MEMBER_NAME");
				char gender = rset.getString("GENDER").charAt(0);
				String email = rset.getString("EMAIL");
				String phone = rset.getString("PHONE");
				int age = rset.getInt("AGE");
				String address = rset.getString("ADDRESS");
				Date enrollDate = rset.getDate("ENROLL_DATE"); // java.sql
				
				Member member = new Member(memberId, memberPwd, memberName, gender, email, phone, age, address, enrollDate);
				list.add(member);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rset);
//			close(stmt);
			close(pstmt);
		}
		
		
		
		return list;
	}
	
	

	public ArrayList<Member> selectGender(Connection conn, char gender) {
		PreparedStatement pstmt = null;
		ResultSet rset = null; // select이기 때문에 들어감
		ArrayList<Member> list = new ArrayList<Member>();
		
		String query = prop.getProperty("selectGender");
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, gender+"");
			rset = pstmt.executeQuery();
			
			while(rset.next()) { 
				// 방법 1
//				String memberId = rset.getString("MEMBER_ID");
//				String memberPwd = rset.getString("MEMBER_PWD");
//				String memberName = rset.getString("MEMBER_NAME");
//				char gender_ = rset.getString("GENDER").charAt(0);
//				String email = rset.getString("EMAIL");
//				String phone = rset.getString("PHONE");
//				int age = rset.getInt("AGE");
//				String address = rset.getString("ADDRESS");
//				Date enrollDate = rset.getDate("ENROLL_DATE"); // java.sql
//				
//				Member member = new Member(memberId, memberPwd, memberName, gender_, email, phone, age, address, enrollDate);
//				list.add(member);
				
				// 방법 2
				Member member = new Member(rset.getString("MEMBER_ID"),
										   rset.getString("MEMBER_PWD"),
										   rset.getString("MEMBER_NAME"),
										   rset.getString("GENDER").charAt(0),
										   rset.getString("EMAIL"),
										   rset.getString("PHONE"),
										   rset.getInt("AGE"),
										   rset.getString("ADDRESS"),
										   rset.getDate("ENROLL_DATE"));
				list.add(member);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return list;
	}


	public int checkMember(Connection conn, String id) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		int result = 0;
		
		String query = prop.getProperty("checkMember");
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, id);
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				result = rset.getInt(1);
				// 조회된 결과 중에 첫 번째 컬럼의 값을 int값/String값으로 가지고 오겠다 => 여기서는 int값
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rset);
			close(pstmt); 
		}
		
		return result;
	}

	public int updateMember(Connection conn, int sel, String id, String input) {
		PreparedStatement pstmt = null;
		int result = 0;

		String query = prop.getProperty("updateMember" + sel); // 번호 누르면 거기로 이동할 수 있도록
		
		try {
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, input);
			pstmt.setString(2, id);
			result = pstmt.executeUpdate(); 
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}

		return result;
	}



	public int deleteMember(Connection conn, String id) {
		PreparedStatement pstmt = null;
		int result = 0;
		
		
		String query = prop.getProperty("deleteMember");
		
		try {
			pstmt = conn.prepareStatement(query);
		
			pstmt.setString(1, id);
			
			result = pstmt.executeUpdate(); 
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return result;
	}


	public ArrayList<Member> selectAll(Connection conn) {
		Statement stmt = null;
		ResultSet rset = null;
		ArrayList<Member> list = new ArrayList<Member>();
		
		String query = prop.getProperty("selectAll");
		
		try {
			stmt = conn.createStatement();
			rset = stmt.executeQuery(query);
			
			while(rset.next()) {
				Member member = new Member(rset.getString("MEMBER_ID"),
										   rset.getString("MEMBER_PWD"),
										   rset.getString("MEMBER_NAME"),
										   rset.getString("GENDER").charAt(0),
										   rset.getString("EMAIL"),
										   rset.getString("PHONE"),
										   rset.getInt("AGE"),
										   rset.getString("ADDRESS"),
										   rset.getDate("ENROLL_DATE"));
				list.add(member);
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rset);
			close(stmt);
		}
		
		return list;
	}


}
