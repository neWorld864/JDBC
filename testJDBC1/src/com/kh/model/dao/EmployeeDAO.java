package com.kh.model.dao;


import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.kh.model.vo.Employee;

public class EmployeeDAO {
	public ArrayList<Employee> selectAll() {
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		ArrayList<Employee> list = new ArrayList<Employee>();
		
		
		// 1. Class.forName() 이용해 JDBC driver 등록(연결)
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// 2. DriverManager.getConnection() 이용해 데이터베이스와 연결작업
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "SCOTT", "SCOTT");
											// 					위치 						아이디 	비밀번호
			
//			System.out.println(conn);
			// DB접속 성공 시 conn에서 DB정보 확인 가능
			// DB접속 실패 시 conn값은 null이 나옴
			
			// 쿼리 작성
			String query = "SELECT * FROM EMP";
			
			// Connection객체를 통해 뽑은 Statement객체로 쿼리 전송 
			stmt = conn.createStatement(); // ==> 쿼리를 작성하고 Connection객체 안에 있는 createStatement()로 Statement객체 만듦
			rset = stmt.executeQuery(query);
			// select 					 - executeQuery() : ResultSet  	==> Statement객체의 executeQuery()메소드를 통해 ResultSet를 반환합니다.
			// DML(insert/update/delete) - executeUpdate() : int  		==> Statement객체의 executeUpdate()메소드를 이용해 int를 반환합니다. 
					/* DML
						drop table emp; -- 실질적으로 반환하는 값 : 숫자 
						ex.@@개 행 이(가) 삭제되었습니다.
					*/
			
			while(rset.next()) { // 다음 값이 있으면 // resultset으로 가져온 컬럼을 그대로 적어야 함
				int empNo = rset.getInt("EMPNO"); // EMPNO의 타입이 NUMBER이기 때문에 getInt
				String empName = rset.getString("ENAME");
				String job = rset.getString("JOB");
				int mgr = rset.getInt("MGR");
				Date hireDate = rset.getDate("HIREDATE");
				int sal = rset.getInt("SAL"); 
				int comm = rset.getInt("COMM");
				int deptNo = rset.getInt("DEPTNO");
				
				Employee emp = new Employee(empNo, empName, job, mgr, hireDate, sal, comm, deptNo);
				list.add(emp); // 내가 원하는 값 다 list 안에 -> menu에서 출력하게 해줘야 함 => ArrayList를 try 밖으로 빼줌
			}
			
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rset.close();
				stmt.close();
				conn.close();
				// 역순으로 닫아줘야 함
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
		
		// 예외처리 안 되어 있음 Unhandled exception type ClassNotFoundException
		// try-catch해줌
		
	}

	public Employee selectEmployee(int empNo) {
		// void를 Employee로 바꿔주는 것 중요
		
		Connection conn = null;
//		Statement stmt = null; // 집어넣을 인자가 적거나 없으면 Statement 사용
		PreparedStatement pstmt = null; // 집어넣을 인자가 많으면 PreparedStatement 사용
		ResultSet rset = null;
		Employee emp = null;
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "SCOTT", "SCOTT");
			
//			String query = "SELECT * FROM EMP WHERE EMPNO = " + empNo;
			String query = "SELECT * FROM EMP WHERE EMPNO = ?";
//			
//			stmt = conn.createStatement(); 
			pstmt = conn.prepareStatement(query); 
			pstmt.setInt(1, empNo);
			
//			rset = stmt.executeQuery(query); // SELECT이기 때문
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				String empName = rset.getString("ENAME");
				String job = rset.getString("JOB");
				int mgr = rset.getInt("MGR");
				Date hireDate = rset.getDate("HIREDATE");
				int sal = rset.getInt("SAL");
				int comm = rset.getInt("COMM");
				int deptNo = rset.getInt("DEPTNO");
				
				emp = new Employee(empNo, empName, job, mgr, hireDate, sal, comm, deptNo);
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rset.close();
//				stmt.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return emp;
	}
	
	public int insertEmployee(Employee emp) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;
		
		
		try {
			// 드라이버 등록
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// 데이터베이스 연결
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "SCOTT", "SCOTT");
			
			// 쿼리문 작성: INSERT INTO EMP VALUSE (empNo, empName, job, mgr, hireDate, sal, comm, deptNo);
//			String query = "insert into EMP values(" + emp.getEmpNo() + ", " + emp.getEmpName() + ", "
//													 + emp.getJob() + ", " + emp.getMgr() + ", "
//													 + "sysdate, " + emp.getSal() + ", "
//													 + emp.getComm() + ", "+ emp.getDeptNo() + ")";
			String query = "INSERT INTO EMP VALUES(?, ?, ?, ?, SYSDATE, ?, ?, ?)";
			
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, emp.getEmpNo());
			pstmt.setString(2, emp.getEmpName());
			pstmt.setString(3, emp.getJob());
			pstmt.setInt(4, emp.getMgr());
			pstmt.setInt(5, emp.getSal());
			pstmt.setInt(6, emp.getComm());
			pstmt.setInt(7, emp.getDeptNo());
			
			result = pstmt.executeUpdate(); // DML구문(INSERT)이기 때문에
			
			// 확정을 지어야 함
			if(result > 0) {
				conn.commit();
			} else {
				conn.rollback();
			}
			
		
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public int updateEmployee(Employee e) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "SCOTT", "SCOTT");
			
			String query = "UPDATE EMP SET JOB = ?, SAL = ?, COMM = ? WHERE EMPNO = ?";
			
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, e.getJob());
			pstmt.setInt(2, e.getSal());
			pstmt.setInt(3, e.getComm());
			pstmt.setInt(4, e.getEmpNo());
			
			result = pstmt.executeUpdate(); 
			
			// 확정을 지어야 함
			if(result > 0) {
				conn.commit();
			} else {
				conn.rollback();
			}
			
	
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return result;
	}

	public int deleteEmployee(int empNo) {
		
		Connection conn = null;
		
		PreparedStatement pstmt = null;
		// [Statement] Statement stmt = null;
		
		int result = 0;
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "SCOTT", "SCOTT");
			
			// String query = "DELETE * FROM EMP WHERE EMPNO = ?"; - DELETE는 * 안 넣음
			// 오류 java.sql.SQLSyntaxErrorException: ORA-00903: invalid table name
			String query = "DELETE FROM EMP WHERE EMPNO = ?";
			// [Statement] String query = "DELETE FROM EMP WHERE EMPNO = " + empNo;
			
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, empNo);
			// [Statement] stmt = conn.createStatement();
			
			result = pstmt.executeUpdate(); 
			// [Statement] result = stmt.executeUpdate(query); 
			
			if(result > 0) {
				conn.commit();
			} else {
				conn.rollback();
			}
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		return result;
	}
	
}
