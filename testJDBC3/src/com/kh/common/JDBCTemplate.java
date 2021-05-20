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

public class JDBCTemplate { // 원래 DAO에서 했는데 빼옴
	
	private static Connection conn = null; // 커넥션 초기화 해주기
	
	private JDBCTemplate() {} // JDBCTemplate 생성자 만들기
	
	public static Connection getConnection() {
		
		if(conn == null) { // connection이 null이면
			
			try {
				Properties prop = new Properties(); // Properties 객체 생성
				
				prop.load(new FileReader("driver.properties")); // "driver.properties"라는 파일을 읽어와라
																// driver, url, id, pwd 담겨있음
				
				// 1. Class.forName() 이용해 JDBC driver 등록(연결)
				Class.forName(prop.getProperty("driver")); 
				// 원래 Class.forName(driver 주소) 해야하지만 이미 driver.properties에 driver주소를 입력해놨으므로 prop에서 driver이라는 property를 가져온다(getProperty)
				
				
				// 2. DriverManager.getConnection() 이용해 데이터베이스와 연결작업
				conn = DriverManager.getConnection(prop.getProperty("url"), prop.getProperty("user"), prop.getProperty("password"));
				// 원래는DriverManager.getConnection(url, user, password) 인데 url, user, password를 prop에 저장해놨으므로 그걸 그대로 가져온다.
				

				conn.setAutoCommit(false); // 자동 commit
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return conn;
		
	}
	
	public static void commit(Connection conn) {
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
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void close(Statement stmt) {
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
	
	public static void close(Connection conn) {
		try {
			if(conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
}
