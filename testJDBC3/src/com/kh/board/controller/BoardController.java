package com.kh.board.controller;

import java.util.ArrayList;

import com.kh.board.model.vo.Board;
import com.kh.board.service.BoardService;
import com.kh.view.View;

public class BoardController {
	private BoardService bService = new BoardService();
	private View view = new View();
	
	public void selectAll() { // 2. 글 목록 조회
		ArrayList<Board> list = bService.selectAll();
		
		if(!list.isEmpty()) {
			view.selectAll(list);
		} else {
			view.displayError("조회 결과가 없습니다.");
		}
	}

	
	public void selectOne() { // 3. 게시물 상세 조회
		int no = view.inputBNO();
		
		Board board = bService.selectOne(no);
		
		if(board != null) {
			view.selectOne(board);
		} else {
			view.displayError("해당 글이 존재하지 않습니다.");
		}
	}
	
	
	public void insertBoard() { // 4. 글 쓰기
		Board board = view.insertBoard();
		
		int result = bService.insertBoard(board);
		
		if(result > 0) {
			view.displaySuccess(result + "개의 행이 추가되었습니다.");
		} else {
			view.displayError("게시글 등록 과정 중 오류 발생");
		}
	}


	public void updateBoard() { // 5. 글 수정
		int no = view.inputBNO(); // 글 번호를 알아야 수정 -> view에서 사용자가 입력한 값을 가져옴
		
		Board board = bService.selectOne(no);
		
		if(board != null) {
			// 내가 쓴 글인지 확인하기
			String memberId = view.getMemberId(); // view에서 사용자가 입력한 아이디를 가져옴. 글쓴이 = 사용자가 되도록
			
			if(memberId.equals(board.getWriter())) { // 사용자(나)가 글쓴이와 일치한다면
				int sel = view.updateMenu(); // 뭐에 대해서 수정할지 번호를 받아옴
				
				String selStr = null;
				String upStr = null;
				
				switch(sel) {
				case 1: // 제목 수정
					selStr = "Title";
					upStr = view.updateTitle();
					break;
				case 2: // 내용 수정
					selStr = "Content";
					upStr = view.updateContent();
					break;
				case 0: return;
				}
				
				int result = bService.updateBoard(no, selStr, upStr);
				
				if(result > 0) {
					view.displaySuccess(result + "개의 행이 수정되었습니다.");
				} else {
					view.displayError("게시글 수정 과정 중 오류 발생");
				}
				
			} else { // 사용자 != 글쓴이
				view.displayError("해당 글을 수정할 수 없습니다"); // 에러 이유 적어주기
			}

		} else { // 해당 번호에 대한 글이 존재하지 않는 경우
			view.displayError("해당 번호의 글이 존재하지 않습니다.");
		}
	}


	public void deleteBoard() { // 6. 글 삭제
		int no = view.inputBNO();
		
		Board board = bService.selectOne(no); // 글 번호를 보내서 걔가 있는지 없는지 확인
		
		if(board != null) { // 글이 있으면
			String memberId = view.getMemberId(); // 입력한 사용자 아이디를 받아옴
			
			if(board.getWriter().equals(memberId)) { // 글쓴이 = 사용자
				char yn = view.deleteBoard(); // 정말 삭제할 건지 다시 한 번 확인
				
				if(yn == 'N') { // n(삭제안함)을 누르면 전 메뉴로 돌아감
					return;
				} else if(yn == 'Y') { // y(삭제함)를 누르면
					int result = bService.deleteBoard(no);
					
					if(result > 0) {
						view.displaySuccess(result + "개의 행이 삭제 되었습니다.");
					} else {
						view.displayError("게시글 삭제 과정 중 오류 발생");
					}
				}
				
			} else { // 글쓴이 != 사용자
				view.displayError("해당 글을 삭제할 수 없습니다");
			}
			
		} else { // 글이 없으면
			view.displayError("해당 번호의 글이 존재하지 않습니다.");
		}
	}

	
}
