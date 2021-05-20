package com.kh.controller;

import java.util.ArrayList;

import com.kh.model.service.MemberService;
import com.kh.model.vo.Member;
import com.kh.view.MemberMenu;

public class MemberController {
	
	private MemberMenu menu = new MemberMenu(); // 멤버메뉴에 대한 객체 만들기
//	private MemberDAO md = new MemberDAO();
	// 서비스 만들고 오기
	private MemberService service = new MemberService(); // 서비스에서 connection을 따로 만듦
	private Member m = new Member();

	
	
	
	public void insertMember() {
		// 새로운 회원 정보를 받기 위한 view로 이동 후 사용자가 입력한 정보 반환 받아옴
		Member mem = menu.insertMember(); // 이렇게 다 쓰고 create하면 반환값도 알아서 설정됨
		
//		int result = md.insertMember(); // 반환값 int(DML 반환값은 int)
		/* insert할때 반환값은 int. insert한 값을 select할때는 반환값이 Member ? */
		int result = service.insertMember(mem); // insert한 값들을 service.insertMember에 보냄 => 값이 입력됐으면 result가 1 이상일 것이고 안 됐으면 0
		
		if(result > 0) {
			menu.displaySuccess(result + "개의 행이 추가되었습니다.");
		} else {
			menu.displayError("데이터 삽입 과정 중 오류 발생");
		}
		
	}

	public void selectAll() {
		ArrayList<Member> list = service.selectAll();
		
		if(!list.isEmpty()) {
			menu.displayMember(list);
		} else {
			menu.displayError("조회 결과가 없습니다.");
		}
	}

	public void selectMember() {
		// 검색 조건을 입력받는 view 호출 후 반환 값 받아오기
		int sel = menu.selectMember(); // 번호 선택한 거 받아옴
		
		// 반환 받은 값(=검색 조건)으로 알맞은 model단을 호출할 수 있도록 switch문 작성
		// 아이디 조회 : 아이디 값 받아오기
		//		일부만 일치해도 검색 조건에 맞음 (r만 검색해도 rose가 나오게)
		// 성별 조회 : 성별 값 받아오기
		// 메인메뉴 : 아무 기능도 수행하지 않고 현재 메소드 종료 -> return
		ArrayList<Member> list = null;
		
		switch(sel) {
		case 1: 
			String id = menu.inputMemberId();
			list = service.selectMemberId(id); // 입력한 id와 일치하는 아이디를 가진 사람의 정보를 list에 저장
			break;
		case 2: 
			char gender = menu.inputGender();
			list = service.selectGender(gender); // 입력한 gender와 일치하는 성별을 가진 사람의 정보를 list에 저장
			break;
		case 0: return;
		}
		
		// 조회 결과가 있을 경우 -> displayMember
		// 조회 결과가 없을 경우 -> displayError : 조회 결과가 없습니다.
		if(!list.isEmpty()) {
			menu.displayMember(list);
		} else {
			menu.displayError("조회 결과가 없습니다.");
		}

	}

	public void updateMember() {
		// 정보를 수정할 회원 선택 : memberId 입력 받기
		String id = menu.inputMemberId(); // menu에서 회원 아이디 입력받은 걸 id에 저장
		
		// 입력 받은 아이디가 존재하는지 확인 
		// -> model단에서 checkMember(memberId:String):int 를 통해 확인
		int check = service.checkMember(id); // service에 checkMember메소드에 id가 있는지 확인 => 있으면 1 없으면 0 반환
		
		// 회원 존재 여부에 따라 처리 방법 지정
		// 조회 결과가 없는 경우 : dispalyError : 입력한 아이디가 존재하지 않습니다.
		// 조회 결과가 있는 경우
		// 수정 종류를 선택할 서브메뉴를 통해 뭐에 대해 수정할 건지 번호 받아옴
		// 0번일 경우 : 현재 메소드 종료
		// 0번이 아닐 경우 : 어떤 값으로 수정할 건지 사용자에게 받아옴
		
	   // 수정 종류 번호, 수정할 회원 아이디, 수정 내용을 모델단에 보냄 
//			
	   // 수정 잘 됐으면 : displaySuccess : result개의 행이 수정되었습니다.
	   // 수정 오류 나면 : displayError : 데이터 수정 과정 중 오류 발생
		
		if(check != 1) {
			menu.displayError("입력한 아이디가 존재하지 않습니다.");
		} else {
			int sel = menu.updateMember();
			
			if(sel == 0) return;
			
			String input = menu.inputUpdate();
			int result = service.updateMember(sel, id, input);
			
			if(result > 0) {
				menu.displaySuccess(result + "개의 행이 수정되었습니다.");
			} else {
				menu.displayError("데이터 수정 과정 중 오류 발생");
			}
		}	
	}
			// 내가 한 방법
//			switch(sel) {
//			case 1: 
//				String newPwd = menu.updateMemberPwd();  // 비밀번호를 입력받으면
//				int np = service.updateMemberPwd(newPwd, id); // 비밀번호를 수정하기 위해 값을 service의 updateMemberPwd에 넘겨주고 수정이 됐으면 1 안 됐으면 0 반환
//				m.setMemberId(id); // 수정할 회원 아이디를 모델단에 넘겨줌
//				
//				if(np > 0) {
//					menu.displaySuccess(np + "개의 행이 수정되었습니다.");
//				} else {
//					menu.displayError("데이터 수정 과정 중 오류 발생");
//				}
//				break;
//				
//				/*
//				 * java.sql.SQLException: 부적합한 열 인덱스
//				   - properties에 " " 이런거 들어갔는지 확인
//				 */
//			case 2: 
//				String newEmail = menu.updateMemberEmail(); 
//				int ne = service.updateMemberEmail(newEmail, id);
//				m.setMemberId(id);
//				
//				
//				if(ne > 0) {
//					menu.displaySuccess(ne + "개의 행이 수정되었습니다.");
//				} else {
//					menu.displayError("데이터 수정 과정 중 오류 발생");
//				}
//				break;
//			case 3: 
//				String newPhone = menu.updateMemberPhone(); 
//				m.setMemberId(id);
//				int nph = service.updateMemberPhone(newPhone, id);
//				
//				if(nph > 0) {
//					menu.displaySuccess(nph + "개의 행이 수정되었습니다.");
//				} else {
//					menu.displayError("데이터 수정 과정 중 오류 발생");
//				}
//				break;
//			case 4: 
//				String newAddress = menu.updateMemberAddress();
//				m.setMemberId(id);
//				int na = service.updateMemberAddress(newAddress, id);
//				
//				if(na > 0) {
//					menu.displaySuccess(na + "개의 행이 수정되었습니다.");
//				} else {
//					menu.displayError("데이터 수정 과정 중 오류 발생");
//				}
//				break;
//			case 0: return;
//			}
		
	

	public void deleteMember() {
	   // 정보를 삭제할 회원 선택 : memberId 입력 받기            
		String id = menu.inputMemberId();
		
	   // 입력 받은 아이디가 존재하는지 확인 
	   // -> model단에서 checkMember(memberId:String):int 를 통해 확인
		int check = service.checkMember(id);
		
	   // 회원 존재 여부에 따라 처리 방법 지정
	   // 조회 결과가 없는 경우 : dispalyError : 입력한 아이디가 존재하지 않습니다.
	   // 조회 결과가 있는 경우
	   //    정말로 삭제할 것인지 확인하는 checkDelete() 호출
	   //   N일 경우 : 현재 메소드 종료
	   //   N이 아닐 경우 : 삭제할 회원의 아이디를 모델단에 보냄

	   // 삭제 잘 됐으면 : displaySuccess : result개의 행이 삭제되었습니다.
	   // 삭제 오류 나면 : displayError : 데이터 삭제 과정 중 오류 발생
		
		if(check != 1) {
			menu.displayError("입력한 아이디가 존재하지 않습니다.");
		} else {
			char yn = menu.checkDelete();
			
			if(yn == 'n') return;
			
			int result = service.deleteMember(id);
			
			if(result > 0) {
				menu.displaySuccess(result + "개의 행이 삭제되었습니다.");
			} else {
				menu.displayError("데이터 삭제 과정 중 오류 발생");
			}
	
		}
	}

	public void exitProgram() {
		service.exitProgram();
	}
	
}
