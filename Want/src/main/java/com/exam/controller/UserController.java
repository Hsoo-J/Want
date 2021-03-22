package com.exam.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.exam.model1.user.UserDAO;
import com.exam.model1.user.UserTO;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

@Controller
public class UserController {


	@Autowired
	private UserDAO userDao;
	

   // 각자 맞는 upload 폴더 경로로 변경
   private String uploadPath = "C:\\Git_Local\\Want\\src\\main\\webapp\\upload";

	// ---------------------- 로그인 관련 ----------------------
	@RequestMapping(value = "/loginForm.do")
	public String loginForm(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws Exception {
		
		request.setCharacterEncoding("utf-8");
		
		if (request.getParameter("login_ok") == null) {
			
		} else if (request.getParameter("login_ok").equals("1") && !request.getParameter("id").equals("")) {

			// =================  일반 로그인 =================
			System.out.println("일반로그인");
			System.out.println(request.getParameter("id"));
			System.out.println(request.getParameter("kakaoemail"));
			
			
			int flag = 2;
			String key = "secret Key";
         

         UserTO userTo = new UserTO();

         String id = request.getParameter("id");
         String pwd = request.getParameter("password");

         userTo.setId(id);

         String realPwd = userDao.loginDecry(userTo);
         String decryPwd = userDao.decryptAES(realPwd, key);

         int result_lookup = userDao.loginLookup(userTo);
         if (result_lookup == 1) { // 회원있음

            if (pwd.equals(decryPwd)) { // 사용자가 적은 pwd와 DB에 저장된 암호화된 비번 복호화해서 비교
               userTo.setPwd(realPwd);
               int result_ok = userDao.loginOk(userTo);

               if (result_ok == 1) { // 비번맞음
                  flag = 0;
               } else if (result_ok == 0) { // 비번틀림
                  flag = 1;
               } else { // 기타오류
                  flag = 3;
               }
            }
         } else if (result_lookup == 0) { // 회원없음
            flag = 2;
         } else { // 기타오류
            flag = 3;
         }
         request.setAttribute("flag", flag);

         // id를 세션에 저장
         session.setAttribute("id", userTo.getId());
         


		} else if (request.getParameter("login_ok").equals("1") && !request.getParameter("kakaoemail").equals("")) {

			// ==================== 카카오 로그인 ========================
			System.out.println("카카오로그인");
			System.out.println(request.getParameter("id"));
			System.out.println(request.getParameter("kakaoemail"));
			System.out.println(request.getParameter("kakaoname"));
			System.out.println(request.getParameter("kakaobirth"));
			
			String kakaoid = request.getParameter("kakaoemail");

         UserTO userTo = new UserTO();

         userTo.setId(kakaoid);

         int result_lookup = userDao.loginLookup(userTo);

         if (result_lookup == 0) { // 회원이 아닌경우 (카카오 계정으로 처음 방문한 경우)

            userTo.setPwd("kakaopwd");
            userTo.setName(request.getParameter("kakaoname"));
            userTo.setBirth(request.getParameter("kakaobirth"));
            userTo.setMail(request.getParameter("kakaoemail"));
            userTo.setPhone("번호를 수정해주세요");
            userTo.setNick(null);

            userTo.setProfile(null);
            userTo.setGreet(null);

            System.out.println(userTo.getName());
            int flag = userDao.signup_ok(userTo);

				
            request.setAttribute("flag", flag);
				
         } else {   // 이미 카카오로 로그인한 적이 있을 때 (최초 1회 로그인때 회원가입된 상태)
            request.setAttribute("flag", 0);
         }
         // kakaoid를 세션에 저장
         session.setAttribute("kakaoid", kakaoid);

      }

      return "user/loginForm";
   }

   // ---------------------- 카카오 로그인 ----------------------
//   @RequestMapping(value = "/kakaologin.do")
//   public String kakaoLogin(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
//      
//      String kakaoid = request.getParameter("kakaoemail");
//      
//      System.out.println(kakaoid);
//      
//      request.setAttribute("flag", 0);
//      
//      // kakaoid를 세션에 저장
//      session.setAttribute("kakaoid", kakaoid);
//      
//      return "user/loginForm";
//   }

   // ---------------------- 로그아웃 ----------------------
   @RequestMapping(value = "/logout.do")
   public String logout_ok(HttpSession session, HttpServletRequest request, Model model) {
	  try {
		request.setCharacterEncoding("utf-8");
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  session.invalidate();
	  return "user/logout_ok";

   }

   // ---------------------- 회원가입관련 ----------------------
   @RequestMapping(value = "/signupForm.do")
   public String signupForm(Model model) {
      return "user/signupForm";
   }

   // signup_ok
   @RequestMapping(value = "/signup_ok.do")
   public String signup_ok(HttpServletRequest request, Model model) throws Exception {

      int maxFileSize = 1024 * 1024 * 6;
      String encType = "utf-8";
      String uploadPath = "C:\\KICKIC\\git repo\\Want\\Want\\src\\main\\webapp\\upload\\profile";

      MultipartRequest multi = null;

      try {
    	  request.setCharacterEncoding("utf-8");

         multi = new MultipartRequest(request, uploadPath, maxFileSize, encType, new DefaultFileRenamePolicy());

         String key = "secret Key";

         UserTO to = new UserTO();
         to.setId(multi.getParameter("id"));

         // 비밀번호암호화해서 TO에 set
         String encryPwd = userDao.encrytAES(multi.getParameter("pwd"), key);
         to.setPwd(encryPwd);

         to.setName(multi.getParameter("name"));
         to.setBirth(multi.getParameter("birth"));
         to.setMail(multi.getParameter("mail"));
         to.setPhone(multi.getParameter("phone"));
         to.setNick(multi.getParameter("nick"));

         to.setProfile(multi.getFilesystemName("profile"));
         File file = multi.getFile("profile");
         if (multi.getParameter("greet").equals("")) {
            to.setGreet(null);
         } else {
            to.setGreet(multi.getParameter("greet"));
         }

         int flag = userDao.signup_ok(to);

         model.addAttribute("flag", flag);
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      return "user/signup_ok";
   }

   // signup에서 id중복조회
   @ResponseBody
   @RequestMapping(value = "/usingId_chk.do", produces = "text/plain")
   public String idCheck(HttpServletRequest request, HttpServletResponse response) {

	   try {
		request.setCharacterEncoding("utf-8");
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   

      String user_id = request.getParameter("user_id");
      UserTO userTo = new UserTO();
      userTo.setId(user_id);

      int using_user = userDao.loginLookup(userTo);
      String result = "" + using_user; // 숫자를 문자열로 변환

      return result;
   }

   // signup에서 닉네임중복조회
   @ResponseBody
   @RequestMapping(value = "/usingNick_chk.do", produces = "text/plain")
   public String nickCheck(HttpServletRequest request, HttpServletResponse response) {
	   try {
		request.setCharacterEncoding("utf-8");
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

      String user_nick = request.getParameter("user_nick");
      UserTO userTo = new UserTO();
      userTo.setNick(user_nick);

      int using_nick = userDao.nickLookup(userTo);
      String result = "" + using_nick;

      return result;
   }

   // 비밀번호찾기
   @RequestMapping(value = "/pwFindForm.do")
   public String pwFindForm(HttpServletRequest request, HttpServletResponse response) {
	   try {
		request.setCharacterEncoding("utf-8");
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

      return "user/pwFindForm";
   }

}