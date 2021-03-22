<%@page import="com.exam.model1.lantripApply.LanTripApplyListTO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.exam.model1.lantripApply.LanTripApplyTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
	LanTripApplyListTO listTO = (LanTripApplyListTO) request.getAttribute("listTO");

	int cpage = listTO.getCpage();
	if(request.getParameter( "cpage" ) != null && !request.getParameter( "cpage" ).equals( "" ) ) {
		cpage = Integer.parseInt( request.getParameter( "cpage" ) );
	}
	int recordPerPage = listTO.getRecordPerPage();
	int totalRecord = listTO.getTotalRecord();
	int totalPage = listTO.getTotalPage();
	int blockPerPage = listTO.getBlockPerPage();

	int startBlock = listTO.getStartBlock();
	int endBlock = listTO.getEndBlock();

	ArrayList<LanTripApplyTO> boardLists = listTO.getBoardLists();
	
	StringBuffer sbHtml = new StringBuffer();
	for (LanTripApplyTO to : boardLists) {
		String no = to.getNo();
		String subject = to.getSubject();
		String writer = to.getWriter();
		String wdate = to.getWdate();
		String hit = to.getHit();
		String location = to.getLocation();
			

		sbHtml.append("<tr>");
		sbHtml.append("   <td>&nbsp;</td>");
		sbHtml.append("   <td id='no'>" + no + "</td>");
		sbHtml.append("	 <th>&nbsp;</th>");
		sbHtml.append("   <td id='loc'>" + location + "</td>");
		sbHtml.append("	 <th>&nbsp;</th>");
		sbHtml.append("   <td>");
		sbHtml.append("      <a href='./lanTrip_apply_view.do?cpage="+cpage+"&no=" + no + "'>" + subject + "</a>&nbsp;");
		sbHtml.append("   </td>");
		sbHtml.append("   <td id='writer'>" + writer + "</td>");
		sbHtml.append("   <td id='wdate'>" + wdate + "</td>");
		sbHtml.append("   <td id='hit'>" + hit + "</td>");
		sbHtml.append("   <td>&nbsp;</td>");
		sbHtml.append("</tr>");
	}	
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Want 랜선여행신청</title>

<jsp:include page="../include/index.jsp"></jsp:include>

<!-- CSS File -->
<link href="./resources/css/lanTrip_apply_list.css?12sdf"
	rel="stylesheet">
<link href="./resources/css/navbar.css" rel="stylesheet">

</head>
<body>
	<!-- 메뉴바 
		 현재페이지 뭔지 param.thisPage에 넣어서 navbar.jsp에  던짐 -->
	<jsp:include page="../include/navbar.jsp">
		<jsp:param value="lanTrip_apply_list" name="thisPage" />
	</jsp:include>

	<br />
	<br />
	<br />
	<div class="con_txt" align="center">
		<div class="contents_sub">
			<div class="board_top">
				<div class="bold" align="left">
					총 <span id="totalRecord"><%=totalRecord%></span>건
				</div>
				<hr />
			</div>

			<!--게시판-->
			<div class="board">
				<table>
					<tr>
						<th width="3%">&nbsp;</th>
						<th width="5%" id="no">번호</th>
						<th width="3%">&nbsp;</th>
						<th width="5%" id="loc">지역</th>
						<th width="3%">&nbsp;</th>
						<th id="subject">제목</th>
						<th width="10%" id="writer">글쓴이</th>
						<th width="17%" id="wdate">등록일</th>
						<th width="5%" id="hit">조회</th>
						<th width="3%">&nbsp;</th>
					</tr>
					<!-- 행 시작 -->
					<%=sbHtml%>


					<!-- 행 끝 -->
				</table>
			</div>
			<div class="card-footer">
			<c:choose>      
				<c:when test="${empty sessionScope.id && empty sessionScope.kakaoid}">
					<button type="button" class="btn btn--radius-2 btn--blue-2 btn-md" onclick="javascript:alert('로그인을 하셔야합니다.')">신청하기</button>
				</c:when>
				<c:otherwise>
					<button type="button" class="btn btn--radius-2 btn--blue-2 btn-md" onclick="location.href='./lanTrip_apply_write.do?cpage=<%=cpage%>'">신청하기</button>	
				</c:otherwise>
			</c:choose>   
			</div>

<%
	if(startBlock == 1) {
        out.println("&lt;&lt;&nbsp;&nbsp;");
	} else {
		out.println("<a href='lanTrip_Apply_list.jsp?cpage=" + (startBlock - blockPerPage) + "'>&lt;&lt;</a>");
	}
	
	if(cpage == 1) {
		out.println("&lt;&nbsp;&nbsp;");    
	} else {
		out.println("<a href='lanTrip_Apply_list.jsp?cpage=" + (cpage - 1)   + "'>&lt;</a>");
	}

	for(int i=startBlock ; i<=endBlock ; i++) {
		if (cpage == i) {
            out.println("<a href='#' style='color: red'>[ " + i + " ]</a>");
       } else {
			out.println("<a href='lanTrip_Apply_list.jsp?cpage=" + i + "'>[ " + i + " ]</a>");
       }
	}
	
	if(cpage == totalPage) {
		out.println("&gt;&nbsp;&nbsp;");    
	} else {
		out.println("<a href='lanTrip_Apply_list.jsp?cpage=" + (cpage + 1)   + "'>&gt;</a>");
	}
	
    if(endBlock == totalPage) {
        out.println("&gt;&gt;&nbsp;");
	} else {
		out.println("<a href='lanTrip_Apply_list.jsp?cpage=" + (startBlock + blockPerPage) + "'>&gt;&gt;</a>");
	}
%>

		</div>
	</div>

</body>
</html>