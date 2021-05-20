package com.kh.common;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JDBCTemplate {
	private static Connection conn = null;
	
	private JDBCTemplate() {}
	
	public static Connection getConnection() {
		
		if(conn == null) {
			try {
//				Class.forName("oracle.jdbc.driver.OracleDriver");
//				
//				conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "MEMBER", "MEMBER");
				
				// file을 따로 만들어서 적어주고 돌아오기
				Properties prop = new Properties(); // properties가 비어있는 상태
				prop.load(new FileReader("driver.properties")); // 외부에 있는 파일(driver.properties)을 읽어와서 채워주는 것
				
				Class.forName(prop.getProperty("driver")); // properties에 driver라는 p
				conn = DriverManager.getConnection(prop.getProperty("url"),
												   prop.getProperty("user"),
												   prop.getProperty("password"));
				
				conn.setAutoCommit(false); // 자동 commit 금지(false) -> 자동 commit이 되면 여러가지가 다 하나의 커넥션에 연결
				
				/*
				 * 이전에 getConnection을 메소드마다 만들어줌 -> 각각의 connection이 다른 메소드에게 영향을 주지 않음
				 * 하지만 지금은 공통으로 하나만 써줌 -> 개발자가 commit을 직접 할 때 수행할 수 있게 만들어줌 => AutoCommit 금지
				 */
				

//			} catch (ClassNotFoundException e) {
//				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		return conn;
	}
	
	public static void commit(Connection conn) { // commit에 대한 메소드를 따로 만들어줌
		try {
			if(conn != null && !conn.isClosed()) {
				conn.commit();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void rollback(Connection conn) {
		try {
			if(conn != null && !conn.isClosed()) {
				conn.rollback();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void close(Connection conn) {
		try {
			if(conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void close(Statement stmt) { // PreparedStatement는 Statement를 상속 -> 다형성이 적용되므로(자식 객체를 부모 클래스 타입 하나로 다룰 수 있는 기술) PreparedStatement는 안 해도 됨
		try {
			if(stmt != null && !stmt.isClosed()) {
				stmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void close(ResultSet rset) {
		try {
			if(rset != null && !rset.isClosed()) {
				rset.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
