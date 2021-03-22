package com.exam.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.exam.model1.shopping.ShoppingDAO;
import com.exam.model1.shopping.ShoppingListTO;
import com.exam.model1.shopping.ShoppingTO;
import com.exam.model1.shoppingComment.ShoppingCommentDAO;
import com.exam.model1.shoppingComment.ShoppingCommentTO;
import com.exam.model1.user.UserDAO;

//import com.exam.model1.BoardListTO;
//import com.exam.model1.CommentDAO;

/**
 * Handles requests for the application home page.
 */
@Controller
public class ShoppingController {
	
	@Autowired
	private ShoppingDAO shopDao;
	
	@Autowired
	private ShoppingCommentDAO shopCommentDao;

	// 각자 맞는 upload 폴더 경로로 변경
	private String uploadPath = "C:\\Git_Local\\Want\\src\\main\\webapp\\upload\\shopping";

	// 쇼핑정보 글쓰기
	@RequestMapping(value = "/shopping_write.do")
	public String shopping_write(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		try {
			request.setCharacterEncoding("utf-8");
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "shopping/shopping_write";
	}

	// 쇼핑정보 글쓰기 Ok
	@RequestMapping(value = "/shopping_write_ok.do")
	public String shopping_write_ok(HttpServletRequest request, HttpServletResponse response) {

		int maxFileSize = 1024 * 1024 * 6;
		String encType = "utf-8";
		String uploadPath = "C:\\Git_Local\\Want\\src\\main\\webapp\\upload\\shopping";
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

			ShoppingTO shopTo = new ShoppingTO();
			shopTo.setWriter(writer);
			shopTo.setSubject(subject);
			shopTo.setContent(content);
			shopTo.setLocation(location);
			shopTo.setPicture(picture);

			int flag = shopDao.shopping_write_ok(shopTo);

			request.setAttribute("flag", flag);
			request.setAttribute("location", shopTo.getLocation());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "shopping/shopping_write_ok";
	}

	// 쇼핑 list
	@RequestMapping(value = "/shopping_list.do")
	public String shopping_list(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("utf-8");
			String location = request.getParameter("location");
			int cpage = 1;
			if (request.getParameter("cpage") != null && !request.getParameter("cpage").equals("")) {
				cpage = Integer.parseInt(request.getParameter("cpage"));
			}

			ShoppingListTO listTO = new ShoppingListTO();
			listTO.setLocation(location);
			listTO.setCpage(cpage);
			
			listTO = shopDao.shopList(listTO);

			request.setAttribute("listTO", listTO);
			request.setAttribute("cpage", cpage);
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "shopping/shopping_list";
	}
	
	// 쇼핑 view
	@RequestMapping(value = "/shopping_view.do")
	public String shopping_view(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("utf-8");
			
			String no = request.getParameter( "no" );
			String cpage = request.getParameter( "cpage" );
			
			//글 내용 가져오기
			ShoppingTO to = new ShoppingTO();
			to.setNo( no );
			to = shopDao.shopView(to);	
			
			request.setAttribute( "to", to );
			
			//해당 글에 대한 댓글 list가져오기
			ShoppingCommentTO commentTo = new ShoppingCommentTO();
			commentTo.setBno( no );
			ArrayList<ShoppingCommentTO> lists = shopCommentDao.shopListComment(commentTo);	
			
			request.setAttribute( "lists", lists );
			request.setAttribute( "cpage", cpage );
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "shopping/shopping_view";
	}
	
	// 쇼핑 view comment ok
	@RequestMapping(value = "/shopping_view_comment_ok.do")
	public String shopping_view_comment_ok(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("utf-8");
			
			String cpage = request.getParameter( "cpage" );
			
			String bno = request.getParameter( "no" );
			String writer = request.getParameter( "cwriter" );
			String content = request.getParameter( "ccontent" );
			
			ShoppingCommentTO commentTo = new ShoppingCommentTO();
			commentTo.setBno(bno);
			commentTo.setWriter(writer);
			commentTo.setContent(content);

			int flag = shopCommentDao.shopViewCommentOk(commentTo);
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

		return "shopping/shopping_view_comment_ok";
	}
	
}
