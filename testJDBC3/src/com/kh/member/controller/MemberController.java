package com.kh.member.controller;

import com.kh.member.model.service.MemberService;
import com.kh.member.model.vo.Member;
import com.kh.view.View;

public class MemberController {
	private MemberService mService = new MemberService();
	private View view = new View();
	
	public void login() { // 1. 로그인
		Member mem = view.inputLogin(); // view에서 넘어온 값들을 mem에 저장
		// 			  'return mem;'을 의미
		
		int result = mService.login(mem); // view에서 넘어온 mem을 MemberService의 login메소드에 고대로 넘겨줌
	
		if(result > 0) {
			view.displayLoginSuccess();
		} else {
			view.displayLoginError();
		}
	
	}
	
	public void exitProgram() { // 0. 프로그램 종료
		mService.exitProgram();
	}
}
