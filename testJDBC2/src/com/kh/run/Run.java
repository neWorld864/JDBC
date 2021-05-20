package com.kh.run;

import com.kh.view.MemberMenu;

public class Run {

	public static void main(String[] args) {
		new MemberMenu().mainMenu();
	}
	
	// java.lang.ClassNotFoundException: oracle.jdbc.driver.OracleDriver
	// ojdbc 등록해야 한다!
	// 프로젝트 오른쪽 버튼 - properties - JAVA BUILD PATH - library - ADD EXTERNAL JARs - ojdbc6 열기 - apply
	
	// java.sql.SQLIntegrityConstraintViolationException: ORA-00001: unique constraint (MEMBER.SYS_C007180) violated
	// primary key가 중복된 듯...
	
	// java.lang.nullpointerexception
	// return null; 이 아닌지 필수 확인!
}
