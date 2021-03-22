package com.exam.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.exam.model1.picture.PictureDAO;
import com.exam.model1.picture.PictureListTO;
import com.exam.model1.picture.PictureTO;
import com.oreilly.servlet.MultipartRequest;


import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

/**
 * Handles requests for the application home page.
 */
@Controller
public class PictureController {
	
	@Autowired
	private PictureDAO pictureDao;
	
	// 각자 맞는 upload 폴더 경로로 변경
	private String uploadPath = "C:\\Git_Local\\Want\\src\\main\\webapp\\upload\\picture";


	// 사진자랑 목록
	@RequestMapping(value = "/picture_list.do")
	public String picture_list(HttpServletRequest request) {
		
		PictureListTO listTO = new PictureListTO();
		listTO.setCpage( Integer.parseInt( request.getParameter( "cpage" ) == null || request.getParameter( "cpage" ).equals( "" ) ? "1" : request.getParameter( "cpage" ) ) );
		listTO = pictureDao.boardList(listTO);
		
		request.setAttribute( "listTO", listTO );
		

		return "picture/picture_list";
	}

	// 사진자랑 글쓰기 form
	@RequestMapping(value = "/picture_write.do")
	public String picture_write(HttpServletRequest request, Model model) {
		try {
			request.setCharacterEncoding("utf-8");
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "picture/picture_write";
	}
	
	// 사진자랑 글쓰기 ok
	@RequestMapping(value = "/picture_write_ok.do")
	public String write_ok(HttpServletRequest request) {
		
	    int maxFileSize = 2048 * 2048 * 6;
	    String encType = "utf-8";
	    
	    MultipartRequest multi = null;
		
	    try {

			multi = new MultipartRequest(request, uploadPath, maxFileSize, encType, new DefaultFileRenamePolicy());
			
			PictureTO to = new PictureTO();
			to.setSubject(multi.getParameter("subject"));
			to.setLocation(multi.getParameter("location"));
			to.setWriter(multi.getParameter("writer"));
			to.setContent(multi.getParameter("content"));
			to.setMedia(multi.getFilesystemName("media"));
	
			int flag = pictureDao.pictureWriteOk(to);
			
			request.setAttribute("flag", flag);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "picture/picture_write_ok";
	}

}
