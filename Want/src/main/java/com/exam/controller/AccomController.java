package com.exam.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.oreilly.servlet.MultipartRequest;


import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.exam.model1.accom.AccomDAO;
import com.exam.model1.accom.AccomListTO;
import com.exam.model1.accom.AccomTO;
import com.exam.model1.accomComment.AccomCommentDAO;
import com.exam.model1.accomComment.AccomCommentTO;
import com.exam.model1.shopping.ShoppingTO;
import com.exam.model1.shoppingComment.ShoppingCommentDAO;
import com.exam.model1.shoppingComment.ShoppingCommentTO;

@Controller
public class AccomController {
	
	@Autowired
	private AccomDAO accomDao;
	
	@Autowired
	private AccomCommentDAO accomCommentDao;

	// 숙소정보 글쓰기
	@RequestMapping(value = "/accom_write.do")
	public String accom_write(HttpServletRequest request, Model model ) {
		try {
			request.setCharacterEncoding("utf-8");
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "accom/accom_write";
	}

	// 숙소정보 글쓰기 Ok
	@RequestMapping(value = "/accom_write_ok.do")
	public String accom_write_ok(HttpServletRequest request, HttpServletResponse response) {
		
		int maxFileSize = 1024 * 1024 * 6;
		String encType = "utf-8";
		String uploadPath = "C:\\Git_Local\\Want\\src\\main\\webapp\\upload\\accom";
		MultipartRequest multi = null;

		try {
			request.setCharacterEncoding("utf-8");
			multi = new MultipartRequest(request, uploadPath, maxFileSize, encType, new DefaultFileRenamePolicy());
			String writer = multi.getParameter("writer");
			String subject = multi.getParameter("subject");
			String content = multi.getParameter("content");
			String location = multi.getParameter("location");
			String picture = multi.getFilesystemName("picture");
			File file = multi.getFile("profile");

			AccomTO accomTo = new AccomTO();
			accomTo.setWriter(writer);
			accomTo.setSubject(subject);
			accomTo.setContent(content);
			accomTo.setLocation(location);
			accomTo.setPicture(picture);

			int flag = accomDao.accom_write_ok(accomTo);

			request.setAttribute("flag", flag);
			request.setAttribute("location", accomTo.getLocation());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "accom/accom_write_ok";
	}

	// 숙소 list
	@RequestMapping(value = "/accom_list.do")
	public String accom_list(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("utf-8");
			String location = request.getParameter("location");
			int cpage = 1;
			if (request.getParameter("cpage") != null && !request.getParameter("cpage").equals("")) {
				cpage = Integer.parseInt(request.getParameter("cpage"));
			}

			AccomListTO listTO = new AccomListTO();
			listTO.setLocation(location);
			listTO.setCpage(cpage);

			listTO = accomDao.accomList(listTO);

			request.setAttribute("listTO", listTO);
			request.setAttribute("cpage", cpage);
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "accom/accom_list";
	}
	
	// 숙소 view
	@RequestMapping(value = "/accom_view.do")
	public String accom_view(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("utf-8");
			
			String no = request.getParameter( "no" );
			String cpage = request.getParameter( "cpage" );
			
			//글 내용 가져오기
			AccomTO to = new AccomTO();
			to.setNo( no );
			to = accomDao.accomView(to);	
			
			request.setAttribute( "to", to );
			
			//해당 글에 대한 댓글 list가져오기
			AccomCommentTO commentTo = new AccomCommentTO();
			commentTo.setBno( no );
			ArrayList<AccomCommentTO> lists = accomCommentDao.accomListComment(commentTo);	
			
			request.setAttribute( "lists", lists );
			request.setAttribute( "cpage", cpage );
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "accom/accom_view";
	}
	
	// 숙소 view comment ok
	@RequestMapping(value = "/accom_view_comment_ok.do")
	public String accom_view_comment_ok(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("utf-8");
			
			String cpage = request.getParameter( "cpage" );
			
			String bno = request.getParameter( "no" );
			String writer = request.getParameter( "cwriter" );
			String content = request.getParameter( "ccontent" );
			
			AccomCommentTO commentTo = new AccomCommentTO();
			commentTo.setBno(bno);
			commentTo.setWriter(writer);
			commentTo.setContent(content);

			int flag = accomCommentDao.accomViewCommentOk(commentTo);
			request.setAttribute( "flag", flag );
			request.setAttribute( "cpage", cpage );
			request.setAttribute( "no", bno );
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "accom/accom_view_comment_ok";
	}
	
}
