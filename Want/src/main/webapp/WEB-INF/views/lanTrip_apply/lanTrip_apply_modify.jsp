<%@page import="com.exam.model1.lantripApply.LanTripApplyTO"%>
<%@page import="com.exam.model1.lantripApply.LanTripApplyDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
	request.setCharacterEncoding( "utf-8" );
	String cpage = request.getParameter( "cpage" );
	
	LanTripApplyTO to = (LanTripApplyTO)request.getAttribute("to");
	
	String no = to.getNo();
	String subject = to.getSubject();
	String content = to.getContent().replaceAll("\n","<br />");
	String writer = to.getWriter();
	String wdate = to.getWdate();
	String location = to.getLocation();
	String picture = to.getPicture();
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><%=subject %> 수정</title>

<jsp:include page="../include/index.jsp"></jsp:include>

<!-- Summernote -->
<script src="./summernote/summernote-lite.js"></script>
<script src="./summernote/lang/summernote-ko-KR.js"></script>

<!-- Summernote CSS File -->
<link rel="stylesheet" href="./summernote/summernote-lite.css">

<!-- CSS File -->
<link href="./resources/css/lanTrip_apply_write.css" rel="stylesheet">
<link href="./resources/css/navbar.css" rel="stylesheet">


<script type="text/javascript">

	window.onload = function() {
		
		document.getElementById('submit1').onclick = function() {
			
				console.log($('#subject').val());
			
				alert('나와라 좀');
				return false;
			}
		
		
		// '등록하기' 버튼 클릭시 모두 입력되었는지 검사
		document.getElementById('submit1').onclick = function() {
			if (document.mfrm.subject.value.trim() == "") {
				alert('제목을 입력하셔야 합니다.');
				return false;
			}

			if (document.mfrm.location.value.trim() == "(선택 안함)") {
				alert('위치를 입력하셔야 합니다.');
				return false;
			}


			// 파일 업로드 확인 메세지
			if (document.mfrm.picture.value.trim() == "") {
				
			} else {
				const extension = document.mfrm.picture.value.split('.').pop();
				if (extension != 'png' && extension != 'jpg'
						&& extension != 'gif' && extension != 'mp4'&& extension != 'PNG'&& extension != 'JPG'&& extension != 'GIF'&& extension != 'MP4'&& extension != 'MOV'&& extension != 'mov') {
					alert('이미지나 동영상 파일을 입력하셔야 합니다.');
					return false;
				}
			}
			
			// 웹 에디터(썸머노트) 입력확인
			if (document.mfrm.content.value.trim() == "") {
				alert('내용을 입력하셔야 합니다.');
				return false;
			} 
			
			document.mfrm.submit();
			
			
		};
		
		// 파일을 선택하면 파일명이 뜨도록 함
		var file_input_container = $('.js-input-file');
		if (file_input_container[0]) {
			file_input_container.each(function() {

				var that = $(this);

				var fileInput = that.find(".input-file");
				var info = that.find(".input-file__info");

				fileInput.on("change", function() {

					var fileName;
					fileName = $(this).val();

					if (fileName.substring(3, 11) == 'fakepath') {
						fileName = fileName.substring(12);
					}

					if (fileName == "") {
						info.text("No file chosen");
					} else {
						info.text(fileName);
					}

				})

			});
		}
		
		// Summernote 설정
		var toolbar = [
		    // 글꼴 설정
		    ['fontname', ['fontname']],
		    // 글자 크기 설정
		    ['fontsize', ['fontsize']],
		    // 굵기, 기울임꼴, 밑줄,취소 선, 서식지우기
		    ['style', ['bold', 'italic', 'underline','strikethrough', 'clear']],
		    // 글자색
		    ['color', ['forecolor','color']],
		    // 표만들기
		    ['table', ['table']],
		    // 글머리 기호, 번호매기기, 문단정렬
		    ['para', ['ul', 'ol', 'paragraph']],
		    // 줄간격
		    ['height', ['height']],
		    // 그림첨부, 링크만들기, 동영상첨부
		    //['insert',['picture','link','video']],
		    //['insert',['link']],
		    // 코드보기, 확대해서보기, 도움말
		    ['view', ['codeview','fullscreen', 'help']]
		  ];
		
		let setting = {
				height: 300,                 // 에디터 높이
				  minHeight: null,             // 최소 높이
				  maxHeight: null,             // 최대 높이
				  focus: true,                  // 에디터 로딩후 포커스를 맞출지 여부
				  lang: "ko-KR",					// 한글 설정
				  placeholder: '최대 2048자까지 쓸 수 있습니다'	//placeholder 설정
		}
		$('.summernote').summernote(setting);
		
	};
	

</script>


</head>
<body>


	<!-- 메뉴바 
		 현재페이지 뭔지 param.thisPage에 넣어서 navbar.jsp에  던짐 -->
	<jsp:include page="../include/navbar.jsp">
		<jsp:param value="lanTrip_apply_list" name="thisPage" />
	</jsp:include>

	<br />
	<div class="page-wrapper bg-light p-t-100 p-b-50">
		<div class="wrapper wrapper--w900">
			<div class="card card-6">

				<div class="card-heading">
					<h2 class="title"><%=subject %> 수정</h2>
				</div>
				<div class="card-body">
					<form action="./lanTrip_apply_modify_ok.do" method="post" name="mfrm" enctype="multipart/form-data">
						
						<c:choose>
							<c:when test="${empty sessionScope.id }">
								<input type="hidden" name="writer" value="${kakaoid}" />
							</c:when>
							<c:otherwise>
								<input type="hidden" name="writer" value="${id}" />
							</c:otherwise>
						</c:choose>
						
						<div class="form-row">
							<div class="name">제목</div>
							<div class="value">
								<input class="input--style-6" type="text" name="subject" id="subject" value="<%=subject %>">
							</div>
						</div>
						<div class="form-row">
							<div class="name">위치</div>
							<div class="value" >
								<label for="location">국가</label>
								<select id="location" name="location" class="form-select">
									<option>(선택 안함)</option>
									<option
										<c:if test="${to.location eq '영국'}">selected</c:if>>
										영국
									</option>
									<option
										<c:if test="${to.location eq '프랑스'}">selected</c:if>>
										프랑스
									</option>
									<option
										<c:if test="${to.location eq '독일'}">selected</c:if>>
										독일
									</option>
									<option
										<c:if test="${to.location eq '이탈리아'}">selected</c:if>>
										이탈리아
									</option>
									<option
										<c:if test="${to.location eq '스위스'}">selected</c:if>>
										스위스
									</option>
									<option
										<c:if test="${to.location eq '그리스'}">selected</c:if>>
										그리스
									</option>
									<option
										<c:if test="${to.location eq '스페인'}">selected</c:if>>
										스페인
									</option>
									<option
										<c:if test="${to.location eq '포르투갈'}">selected</c:if>>
										포르투갈
									</option>
									<option
										<c:if test="${to.location eq '체코'}">selected</c:if>>
										체코
									</option>
									<option
										<c:if test="${to.location eq '헝가리'}">selected</c:if>>
										헝가리
									</option>
									<option
										<c:if test="${to.location eq '오스트리아'}">selected</c:if>>
										오스트리아
									</option>
									<option
										<c:if test="${to.location eq '스웨덴'}">selected</c:if>>
										스웨덴
									</option>
									<option
										<c:if test="${to.location eq '핀란드'}">selected</c:if>>
										핀란드
									</option>
									<option
										<c:if test="${to.location eq '폴란드'}">selected</c:if>>
										폴란드
									</option>
								</select>
							</div>
						</div>
						<div class="form-row">
							<div class="name">파일 업로드</div>
							<div class="value">
								<div class="input-group js-input-file">
									<input class="input-file" type="file" name="picture" id="file">
									<label class="label--file" for="file">파일 선택</label> 
									<span class="input-file__info"><%=picture %></span>
								</div>
								<div class="label--desc">여행 사진을 업로드하세요. 최대파일 크기는
									00MB입니다.</div>
							</div>
						</div>
						
						<div class="form-row">
							<div class="name">내용</div>
							<div class="value">
								<div class="summernote-group">
									<div class="summernote" name="content" id="content"><%=content %></div>
								</div>
							</div>
						</div>
					</form>
				</div>
				<div class="card-footer">
					<c:choose>      
						<c:when test="${empty sessionScope.id && empty sessionScope.kakaoid}">
							<button type="submit" id="submit1" class="btn btn--radius-2 btn--blue-2" onclick="javascript:alert('로그인을 하셔야합니다.')">수정하기</button>
						</c:when>
					<c:otherwise>
						<button type="submit" id="submit1" class="btn btn--radius-2 btn--blue-2" onclick="location.href='./lanTrip_apply_modify.do?cpage=<%=cpage%> '">수정하기</button>
					</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
	</div>

</body>
</html>