
package com.exam.controller;


import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.exam.model1.lantrip.LanTripDAO;
import com.exam.model1.lantrip.LanTripListTO;
import com.exam.model1.lantrip.LanTripTO;
import com.exam.model1.user.UserDAO;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

@Controller
public class LanTripController {


   @Autowired
   private LanTripDAO dao;

   // 각자 맞는 upload 폴더 경로로 변경
   private String uploadPath = "C:\\Git_Local\\Want\\src\\main\\webapp\\upload\\lanTrip";
   

   // 랜선여행 목록
   @RequestMapping(value = "/lanTrip_list.do")
   public String lanTrip_list(HttpServletRequest request, Model model) {
      
      LanTripListTO listTO = new LanTripListTO();
      listTO.setCpage( Integer.parseInt( request.getParameter( "cpage" ) == null || request.getParameter( "cpage" ).equals( "" ) ? "1" : request.getParameter( "cpage" ) ) );
      listTO = dao.lanTripList(listTO);
      
      model.addAttribute( "listTO", listTO );
      
      return "lanTrip/lanTrip_list";
   }
   

   // 랜선여행 write
   @RequestMapping(value = "/lanTrip_write.do")
   public String lanTrip_write(Model model) {
      return "lanTrip/lanTrip_write";
   }
   
   // 랜선여행 write_ok
   @RequestMapping(value = "/lanTrip_write_ok.do")
   public String lanTrip_write_ok(HttpServletRequest request, Model model) {
	    String encType = "utf-8";
	    int maxFileSize = 2048 * 2048 * 6;
	    
	    MultipartRequest multi = null;
		
	    try {
			multi = new MultipartRequest(request, uploadPath, maxFileSize, encType, new DefaultFileRenamePolicy());
			
			LanTripTO to = new LanTripTO();
			to.setSubject(multi.getParameter("subject"));
			to.setLocation(multi.getParameter("location"));
			to.setWriter(multi.getParameter("writer"));
			to.setContent(multi.getParameter("content"));
			to.setVideo(multi.getFilesystemName("video"));
			to.setWdate(multi.getParameter("wdate"));
	
			int flag = dao.boardWriteOk(to);
			
			request.setAttribute("flag", flag);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   return "lanTrip/lanTrip_write_ok";
   }
   
   // 랜선여행 게시물 view
   @RequestMapping(value = "/lanTrip_view.do")
   public String lanTrip_view(HttpServletRequest request) {
	   
	   String no = request.getParameter( "no" );
	   LanTripTO to = new LanTripTO();
	   
	   to.setNo(request.getParameter( "no" ));
	   
	   
	   to = dao.boardView(to);
	   
	   request.setAttribute("to", to);
	   
      return "lanTrip/lanTrip_view";
   }

}