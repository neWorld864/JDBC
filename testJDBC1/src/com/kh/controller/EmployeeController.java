package com.kh.controller;

import java.util.ArrayList;

import com.kh.model.dao.EmployeeDAO;
import com.kh.model.vo.Employee;
import com.kh.view.Menu;

public class EmployeeController {
	private EmployeeDAO empDAO = new EmployeeDAO();
	private Menu menu = new Menu();
	
	public void selectAll() {
		
		ArrayList<Employee> list = empDAO.selectAll();
		/* ArrayList는 List 인터페이스를 상속받은 클래스로 크기가 가변적으로 변하는 선형리스트
		   ArrayList는 객체들이 추가되어 저장 용량(capacity)을 초과한다면 자동으로 부족한 크기만큼 저장 용량(capacity)이 늘어난다  */
		
		if(list.isEmpty()) {
			menu.displayError("조회 결과가 없습니다.");
		} else {
			menu.selectAll(list);
		}
	}

	public void selectEmployee() { // Menu에 'public void selectEmpNo() {}' 생성(밑줄 뜨는 거 누르면 됨)
		// 사번 입력 view 호출 후 입력된 사번 받아옴
		int empNo = menu.selectEmpNo();
		
		// 받아온 사번을 DAO에 넘기고 DAO가 반환하는 사원 정보를 받아옴
		Employee emp = empDAO.selectEmployee(empNo);
		
		if(emp != null) {
			menu.selectEmployee(emp);
		} else {
			menu.displayError("해당 사번의 검색 결과가 없습니다.");
		}
	}
	
	public void insertEmployee() {
		// 사원 정보 입력 view 호출 후 입력된 정보 받아옴
		Employee emp = menu.insertEmployee();
		
		// 받아온 정보를 DAO에 넘기고 DAO가 반환하는 값을 이용해 결과 view 출력 
		int result = empDAO.insertEmployee(emp);
		
		if(result > 0) {
			menu.displaySuccess(result + "개의 행이 추가되었습니다.");
		} else {
			menu.displayError("데이터 삽입 과정 중 오류 발생");
		}
		
	}

	public void updateEmployee() {
		
		int empNo = menu.selectEmpNo(); // menu.selectEmpNo()에서 입력한 사번을 empNo에 저장
		
		Employee e = menu.updateEmployee(); // menu.updateEmployee()에서 수정한 직책, 급여, 커미션을 Employee객체에 저장
		e.setEmpNo(empNo);
		
		int result = empDAO.updateEmployee(e);
		
		if(result > 0) {
			menu.displaySuccess(result + "개의 행이 수정되었습니다.");
		} else {
			menu.displayError("데이터 수정 과정 중 오류 발생");
		}
	}

	public void deleteEmployee() {
		int empNo = menu.selectEmpNo();
		
		char check = menu.deleteEmployee();
		
		if(check == 'y') {
			int result = empDAO.deleteEmployee(empNo); // empDAO.deleteEmployee(empNo);까지만 만들었다가 EmployeeDAO 만들고 다시 돌아와서 int result로 받아주고 if 만들어줌 큰 if else는 기존에 만들어 놨어야
			
			if(result > 0) {
				menu.displaySuccess(result + "개의 행이 삭제되었습니다.");
			} else {
				menu.displayError("데이터 삭제 과정 중 오류 발생");
			}
			
			
		} else if (check == 'n') {
			menu.displaySuccess("사원 정보 삭제 취소");
		} else {
			menu.displayError("잘못 입력하셨습니다. (y 또는 n 입력)");
		}
	}
	
}
