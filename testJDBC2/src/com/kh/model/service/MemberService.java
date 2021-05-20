package com.kh.model.service;

import java.sql.Connection;
import java.util.ArrayList;

import static com.kh.common.JDBCTemplate.getConnection;
import static com.kh.common.JDBCTemplate.commit;
import static com.kh.common.JDBCTemplate.rollback;
import static com.kh.common.JDBCTemplate.close;
import com.kh.model.dao.MemberDAO;
import com.kh.model.vo.Member;

public class MemberService {

	public int insertMember(Member member) {
		
		Connection conn = getConnection();
		
		MemberDAO dao = new MemberDAO();
		
		int result = dao.insertMember(conn, member);
		// 쿼리 전송(커넥션 객체를 통해 전송) 위해서는 커넥션을 받아와서 dao에 넘겨준다
		// member를 같이 받아와야 같이 저장함 .?
		
		if(result > 0) {
			commit(conn);
		} else {
			rollback(conn);
		}
		
		return result; // return이 0이 아닌지 꼭 확인할 것!
	}

	public ArrayList<Member> selectMemberId(String id) {
		Connection conn = getConnection();
		MemberDAO dao = new MemberDAO();
		
		ArrayList<Member> list = dao.selectMemberId(conn, id);
		
		return list;
	}

	public ArrayList<Member> selectGender(char gender) {
		Connection conn = getConnection();
		MemberDAO dao = new MemberDAO();
		
		ArrayList<Member> list = dao.selectGender(conn, gender);
		
		return list;
	}

	public int checkMember(String id) {
		Connection conn = getConnection();
		MemberDAO dao = new MemberDAO();

		int result = dao.checkMember(conn, id); // int값이지만 select일 수 있겠다고 생각
		return result;
	}
	
	public int updateMember(int sel, String id, String input) {
		Connection conn = getConnection();
		MemberDAO dao = new MemberDAO();
		
		int result = dao.updateMember(conn, sel, id, input);
		
		if(result > 0) {
			commit(conn);
		} else {
			rollback(conn);
		}	
				
		return result;
	}
	
	// 내가 한 방법
//	public int updateMemberPwd(String newPwd, String id) {
//		Connection conn = getConnection();
//		MemberDAO dao = new MemberDAO();
//		
//		int result = dao.updateMemberPwd(conn, newPwd, id);
//		
//		if(result > 0) {
//			commit(conn);
//		} else {
//			rollback(conn);
//		}
//		
//		return result;
//		
//	}
//
//	public int updateMemberEmail(String newEmail, String id) {
//		Connection conn = getConnection();
//		MemberDAO dao = new MemberDAO();
//		
//		int result = dao.updateMemberEmail(conn, newEmail, id);
//		
//		if(result > 0) {
//			commit(conn);
//		} else {
//			rollback(conn);
//		}
//		
//		return result;
//	}
//
//	public int updateMemberPhone(String newPhone, String id) {
//		Connection conn = getConnection();
//		MemberDAO dao = new MemberDAO();
//		
//		int result = dao.updateMemberPhone(conn, newPhone, id);
//		
//		if(result > 0) {
//			commit(conn);
//		} else {
//			rollback(conn);
//		}
//		
//		return result;
//	}
//
//	public int updateMemberAddress(String newAddress, String id) {
//		Connection conn = getConnection();
//		MemberDAO dao = new MemberDAO();
//		
//		int result = dao.updateMemberAddress(conn, newAddress, id);
//		
//		if(result > 0) {
//			commit(conn);
//		} else {
//			rollback(conn);
//		}
//		
//		return result;
//	}



	public int deleteMember(String id) {
		Connection conn = getConnection();
		MemberDAO dao = new MemberDAO();
		
		int result = dao.deleteMember(conn, id);
		
		if(result > 0) {
			commit(conn);
		} else {
			rollback(conn);
		}
		
		return result;
	}

	public ArrayList<Member> selectAll() {
		Connection conn = getConnection();
		MemberDAO dao = new MemberDAO();
		ArrayList<Member> list = dao.selectAll(conn);
		return list;
	}

	public void exitProgram() {
		Connection conn = getConnection();
		close(conn);
		// import static com.kh.common.JDBCTemplate.close;를 직접 추가할 것!!!
		
	}

}
