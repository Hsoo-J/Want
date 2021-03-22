package com.exam.controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;




@Controller
public class ProfileController {
	
	// 각자 맞는 upload 폴더 경로로 변경
	private String uploadPath = "C:\\Git_Local\\Want\\src\\main\\webapp\\upload\\profile";
	
	// 내 프로필
	@RequestMapping(value = "/profile.do")
	public String profile(HttpServletRequest request, Model model ) {
		try {
			request.setCharacterEncoding("utf-8");
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "profile/profile";
	}
	


}
