<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 

<%
	int flag = (Integer)request.getAttribute("flag");
	out.println("<script type='text/javascript'>");
	if(flag == 0){
		out.println("alert('수정에 성공했습니다.');");
		out.println("location.href='./with_list.do?no="+request.getAttribute("no")+"';");
			
	}else{
		out.println("alert('수정에 실패했습니다.');");
		out.println("history.back();");
	}
	out.println("</script>");
%>