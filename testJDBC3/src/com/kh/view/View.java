package com.kh.view;

import java.util.ArrayList;
import java.util.Scanner;

import com.kh.board.controller.BoardController;
import com.kh.board.model.vo.Board;
import com.kh.member.controller.MemberController;
import com.kh.member.model.vo.Member;

public class View {
	private Scanner sc = new Scanner(System.in);
	private static Member mem = null;
	
	public void mainMenu() {
		MemberController mc = new MemberController();
		BoardController bc = new BoardController();
		
		int select = 0;
		do {
			System.out.println("\n *** 게시판 프로그램 *** \n");
		
			if(mem == null) { // 아무도 로그인을 하지 않은 경우는 객체가 null이다
				System.out.println("1. 로그인");
				System.out.println("0. 프로그램 종료");
				System.out.print("번호 선택 : ");
				select = Integer.parseInt(sc.nextLine());
				
				switch(select) {
				case 1: mc.login(); break;
				case 0: mc.exitProgram(); System.out.println("프로그램을 종료합니다."); break;
				default: System.out.println("잘못된 번호입니다. 다시 입력해주세요.");
				}
			} else { // 로그인을 하면 mem에 객체가 더 이상 null값이 아니므로 -> 로그인 한 경우의 메뉴창
				System.out.println("1. 로그아웃");
				System.out.println("2. 글 목록 조회");
				System.out.println("3. 게시물 상세 조회");
				System.out.println("4. 글 쓰기");
				System.out.println("5. 글 수정");
				System.out.println("6. 글 삭제");
				System.out.println("0. 프로그램 종료");
				System.out.print("번호 선택 : ");
				select  = Integer.parseInt(sc.nextLine());
				
				switch(select) {
				case 1: System.out.println("<<로그아웃>>"); mem = null; break; // mem을 null로 꼭 만들어 주어야 한다 -> 안 그러면 로그아웃을 했다고 해서 mem != null이 되는 것이 아니기 때문에 계속 로그인된 상태로 존재할 것이다
				case 2: bc.selectAll(); break;
				case 3: bc.selectOne(); break;
				case 4: bc.insertBoard(); break;
				case 5: bc.updateBoard(); break;
				case 6: bc.deleteBoard(); break;
				case 0: mc.exitProgram(); System.out.println("프로그램을 종료합니다."); break;
				default: System.out.println("잘못된 번호입니다. 다시 입력해주세요");
				}
				
			}
		} while(select != 0);
	}

	
	
	
	
	// Member
	
	public Member inputLogin() { // 1. 로그인
		mem = new Member(); // 무조건적으로 멤버 객체 생성 -> 잘못된 것을 입력해도 객체를 생성함 => 더 이상 mem 값이 null이 아니게 됨 -> 잘못된 것을 입력했음에도 else 항목으로 넘어가게됨 ==> displayLoginError()에서 mem=null;이 유지되게 만들어줘야 함
		
		System.out.println("----- 로그인 -----");
		System.out.print("ID : ");
		mem.setMemberId(sc.nextLine()); // 입력받은 ID값을 Member객체의 MemberId에 집어넣기(대입)
		
		System.out.print("PW : ");
		mem.setMemberPwd(sc.nextLine()); // 입력받은 PW값을 Member객체의 MemberPwd에 집어넣기(대입)
		

		return mem;
	}

	public void displayLoginSuccess() { // 로그인 성공 시 뜰 창
		System.out.println(mem.getMemberId() + "님 환영합니다.");
	}

	public void displayLoginError() {
		mem = null; // 잘못 입력하면 mem값이 null로 유지되도록 만들어줌 -> 이게 없으면 잘못 입력해도 mem != null이 되어 else 항목으로 넘어가게 됨
		System.out.println("로그인 정보를 확인해주세요.");
	}

	
//----------------------------------------------------------------------------------------------------------------------	
	// Board
	
	public void selectAll(ArrayList<Board> list) { // 2. 글 목록 조회
		System.out.printf("%-3s %-15s %-10s %-15s\n", "BNO", "TITLE", "WRITER", "CREATE_DATE");
		System.out.println("------------------------------------------");
		for(Board b : list) {
			System.out.printf("%-3d %-15s %-10s %-15s\n", b.getbNo(), b.getTitle(), b.getWriter(), b.getCreateDate());
		}
	}

	public void displayError(String string) {
		System.out.println("서비스 요청 실패 : " + string);
	}
	// 오류가 뜬다면 서비스 요청 실패 : 조회 결과가 없습니다. 라고 뜸
	// 여기서 string은 BoardController에 있는 
	// 'view.displayError("조회 결과가 없습니다.");'의 "조회 결과가 없습니다."가 string임
	
	
	public void selectOne(Board board) { // 3. 게시물 상세 조회
		System.out.println();
		System.out.println("-------------------------------------------------");
		System.out.println("글번호 : " + board.getbNo());
		System.out.println("제목 : " + board.getTitle());
		System.out.printf("작성자 : %-10s 작성일 %-15s\n",board.getWriter(), board.getCreateDate());
		System.out.println("-------------------------------------------------");
		System.out.println(board.getContent());
		System.out.println("-------------------------------------------------");
	}
	
	
	public Board insertBoard() { // 4. 글 쓰기
		System.out.print("제목 : ");
		String title = sc.nextLine();
		
		StringBuffer content = new StringBuffer();
		StringBuffer str = new StringBuffer();
		System.out.println("-------- 내용 입력(종료 시 exit 입력) -----------");
		while(true) {
			str.delete(0,  str.capacity());
			str.append(sc.nextLine());
			
			if(str.toString().toLowerCase().equals("exit")) break;
			
			content.append(str);
			content.append("\n");
			
		}
		
		Board board = new Board(title, content.toString(), mem.getMemberId());
		
		return board;
	}

	public void displaySuccess(String string) {
		System.out.println("서비스 요청 성공 : " + string);
	}

	public int inputBNO() {
		System.out.print("글 번호 입력 : ");
		int bNo = Integer.parseInt(sc.nextLine());
		
		return bNo;
	}




	public int updateMenu() { // 5. 글 수정
		int sel = 0;
		while(true) {
			System.out.println("1. 제목 수정");
			System.out.println("2. 내용 수정");
			System.out.println("0. 메인 메뉴로 이동");
			System.out.print("번호 선택 : ");
			sel = Integer.parseInt(sc.nextLine());
			
			switch(sel) {
			case 1: case 2: case 0: return sel;
			default: System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
			}
		}
	}

	public String getMemberId() {
		return mem.getMemberId(); // 객체에 저장되어 있는 아이디 가져오기
	}

	
	// 5-1. 제목 수정
	public String updateTitle() {
		System.out.println("제목 : ");
		String title = sc.nextLine();
		return title;
	}
	
	// 5-2. 내용 수정
	public String updateContent() {
		StringBuffer content = new StringBuffer(); // StringBuffer = String의 상위호환버전이라고 생각하면 됨
		StringBuffer str = new StringBuffer();
		System.out.println("---------- 내용 입력(종료 시 exit 입력) ----------");
		while(true) {
			str.delete(0, str.capacity()); // delete(int start, int end) : 인덱스 0부터 str길이만큼 기존 str을 삭제하고
			str.append(sc.nextLine()); // 새로 내용 쓴 걸로 붙여넣기
			
			if(str.toString().toLowerCase().equals("exit")) { // exit 누르면 종료하도록
				break; 
			}
			
			content.append(str); // 새로 쓴 str을 content에 추가
			content.append("\n");
		}
		return content.toString();
	}




	// 6. 글 삭제
	public char deleteBoard() { // 6. 글 삭제
		System.out.print("삭제 하시겠습니까?(Y/N) : ");
		char yn = sc.nextLine().toUpperCase().charAt(0);
		
		return yn;
	}


	
	

}
