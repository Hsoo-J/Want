package com.exam.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.exam.model1.lantripApply.LanTripApplyDAO;
import com.exam.model1.lantripApply.LanTripApplyListTO;
import com.exam.model1.lantripApply.LanTripApplyTO;
import com.exam.model1.picture.PictureTO;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

@Controller
public class LantripApplyController {

	// 랜선여행 신청 목록
	@Autowired
	private LanTripApplyDAO dao;

	private String uploadPath = "C:\\Git_Local\\Want\\src\\main\\webapp\\upload";

	@RequestMapping(value = "/lanTrip_apply_list.do")
	public String list(HttpServletRequest request, Model model) {
		try {
			request.setCharacterEncoding("utf-8");
			LanTripApplyListTO listTO = new LanTripApplyListTO();
			listTO.setCpage(Integer
					.parseInt(request.getParameter("cpage") == null || request.getParameter("cpage").equals("") ? "1"
							: request.getParameter("cpage")));
			listTO = dao.boardList(listTO);

			model.addAttribute("listTO", listTO);

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "lanTrip_apply/lanTrip_apply_list";
	}

	// lanTrip_apply_write
	@RequestMapping(value = "/lanTrip_apply_write.do")
	public String lanTrip_apply_write(HttpServletRequest request, Model model) {
		try {
			request.setCharacterEncoding("utf-8");

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "lanTrip_apply/lanTrip_apply_write";
	}

	// lanTrip_apply_write_ok
	@RequestMapping(value = "/lanTrip_apply_write_ok.do")
	public String lanTrip_Apply_write_ok(HttpServletRequest request) {

		int maxFileSize = 2048 * 2048 * 6;
		String encType = "utf-8";

		MultipartRequest multi = null;

		try {
			multi = new MultipartRequest(request, uploadPath + "\\lanTrip_apply", maxFileSize, encType,
					new DefaultFileRenamePolicy());

			LanTripApplyTO to = new LanTripApplyTO();
			to.setSubject(multi.getParameter("subject"));
			to.setLocation(multi.getParameter("location"));
			to.setWriter(multi.getParameter("writer"));
			to.setContent(multi.getParameter("content"));
			to.setPicture(multi.getFilesystemName("picture"));

			int flag = dao.lanTripApplyWriteOk(to);

			request.setAttribute("flag", flag);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "lanTrip_apply/lanTrip_apply_write_ok";
	}

	// lanTrip_apply_view
	@RequestMapping(value = "/lanTrip_apply_view.do")
	public String lanTrip_apply_view(HttpServletRequest request) {

		String no = request.getParameter("no");
		LanTripApplyTO to = new LanTripApplyTO();

		to.setNo(request.getParameter("no"));

		to = dao.lanTrip_apply_View(to);

		request.setAttribute("to", to);

		return "lanTrip_apply/lanTrip_apply_view";
	}

	// lanTrip_apply_delete_ok
	@RequestMapping(value = "/lanTrip_apply_delete_ok.do")
	public String lanTrip_apply_delete(HttpServletRequest request) {

		String no = request.getParameter("no");
		LanTripApplyTO to = new LanTripApplyTO();

		to.setNo(request.getParameter("no"));

		int flag = dao.lanTrip_apply_delete_ok(to);

		request.setAttribute("flag", flag);

		return "lanTrip_apply/lanTrip_apply_delete_ok";
	}

	// lanTrip_apply_modify
	@RequestMapping(value = "/lanTrip_apply_modify.do")
	public String lanTrip_apply_modify(HttpServletRequest request) {
		
		LanTripApplyTO to = new LanTripApplyTO();

		to.setNo(request.getParameter("no"));

		to = dao.lanTrip_apply_modify(to);

		request.setAttribute("to", to);

		return "lanTrip_apply/lanTrip_apply_modify";
	}
	
	// lanTrip_apply_modify_ok
	@RequestMapping(value = "/lanTrip_apply_modify_ok.do")
	public String lanTrip_apply_modify_ok(HttpServletRequest request) {

		
		int maxFileSize = 2048 * 2048 * 6;
		String encType = "utf-8";

		MultipartRequest multi = null;

		try {
			multi = new MultipartRequest(request, uploadPath + "\\lanTrip_apply", maxFileSize, encType,
					new DefaultFileRenamePolicy());

			LanTripApplyTO to = new LanTripApplyTO();
			to.setNo(multi.getParameter("no"));
			to.setSubject(multi.getParameter("subject"));
			to.setLocation(multi.getParameter("location"));
			to.setWriter(multi.getParameter("writer"));
			to.setContent(multi.getParameter("content"));
			to.setPicture(multi.getFilesystemName("picture"));

			int flag = dao.lanTrip_apply_modify_ok(to);

			request.setAttribute("flag", flag);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "lanTrip_apply/lanTrip_apply_modify_ok";
	}

}
