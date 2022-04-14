<%@ page import = "dao.BoardDaoImpl" %>
<%@ page import = "dao.BoardDao" %>
<%@ page import = "vo.BoardVo" %>
<%@ page import = "java.util.ArrayList" %>
<%@ page import = "java.util.List" %>
<%@ page import="java.io.PrintWriter"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<% 
	/* BoardDaoImpl dao = new BoardDaoImpl();
	String strPg = request.getParameter("pg");
	
	System.out.println(strPg); // 얘를 어디서 받아와야 하지... 
	
	int rowSize = 10;
	int pg = 1;
	
	if(strPg != null){
		pg = Integer.parseInt(strPg);
	}
	
	int from = (pg * rowSize) - (rowSize-1);
	int to = (pg * rowSize);
	
	List<BoardVo> list = dao.getList(from, to);
	
	int total = dao.getTotal();
	int allPage = (int) Math.ceil(total/(double)rowSize);
	int block = 10;
	
	System.out.println("전체 페이지수 : "+allPage);
	System.out.println("현재 페이지 : "+ strPg);
	 
	int fromPage = ((pg-1)/block*block)+1;  //보여줄 페이지의 시작
	int toPage = ((pg-1)/block*block)+block; //보여줄 페이지의 끝
	if(toPage> allPage){ // 예) 20>17
		toPage = allPage;
	}
	   
	System.out.println("페이지시작 : "+fromPage+ " / 페이지 끝 :"+toPage);    
	
	pageContext.setAttribute("list", list); */

%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="/mysite/assets/css/board.css" rel="stylesheet" type="text/css">
<title>Mysite</title>
</head>
<body>
	<div id="container">
		
		<c:import url="/WEB-INF/views/includes/header.jsp"></c:import>
		<c:import url="/WEB-INF/views/includes/navigation.jsp"></c:import>
		
		<div id="content">
			<div id="board">
				<form id="search_form" action="/mysite/board" method="post">
					<input type="hidden" name="a" value="search">
					<select style="heiht:30px" name="field">
						<option value = "0">선택</option>
						<option value = "userName">글쓴이</option>
						<option value = "title">제목</option>
						<option value = "content">내용</option>
						<option value = "reg_date">날짜</option>
						<option value = "file">첨부파일</option>
					</select>
					<input type="text" id="kwd" name="kwd" value="">
					<input type="submit" value="찾기">
				</form>
				<table class="tbl-ex">
					<tr>
						<th>번호</th>
						<th>제목</th>
						<th>글쓴이</th>
						<th>조회수</th>
						<th>작성일</th>
						<th>&nbsp;</th>
					</tr>	
					<c:forEach items="${list }" var="vo">
						<tr>
							<td>${vo.no }</td>
							<td><a href="/mysite/board?a=read&no=${vo.no }">${vo.title }</a></td>
							<td>${vo.userName }</td>
							<td>${vo.hit }</td>
							<fmt:parseDate value='${ vo.regDate }' var='regDate' pattern="yyyy-MM-dd H:m" />
							<td><fmt:formatDate value="${ regDate }" pattern="yy-MM-dd HH:mm"/></td>
							<td>
								<c:if test="${authUser.no == vo.userNo }">
									<a href="/mysite/board?a=delete&no=${vo.no }" class="del">삭제</a>
								</c:if>
							</td>
						</tr>
					</c:forEach>
				</table>
				<div class="pager">
					<ul>
					
					<c:if test="${pg>block}">
						<li><a href="/mysite/board?a=${null eq a ? 'list' : 'search' }&pg=1&field=${field}&kwd=${kwd}">◀◀</a></li>
                		<li><a href="/mysite/board?a=${null eq a ? 'list' : 'search' }&pg=${fromPage-1}&field=${field}&kwd=${kwd}">◀</a></li>
                	</c:if>
					<c:forEach var = "i" begin="${fromPage}" end="${toPage }">
						<c:choose>
							<c:when test="${i==pg}">
								<li class="selected">${i}</li>
							</c:when>
							<c:otherwise>
								<li><a href="/mysite/board?a=${null eq a ? 'list' : 'search' }&pg=${i}&field=${field}&kwd=${kwd}">${i}</a></li>
							</c:otherwise>
						</c:choose>
					</c:forEach>
					
				
					<c:if test="${toPage<allPage}">
						<li><a href="/mysite/board?a=${null eq a ? 'list' : 'search' }&pg=${toPage+1}&field=${field}&kwd=${kwd}">▶</a></li>
                		<li><a href="/mysite/board?a=${null eq a ? 'list' : 'search' }&pg=${allPage}&field=${field}&kwd=${kwd}">▶▶</a></li>
					</c:if>
					
						
					</ul>
				</div>				
				<c:if test="${authUser != null }">
					<div class="bottom">
						<a href="/mysite/board?a=writeform" id="new-book">글쓰기</a>
					</div>
				</c:if>				
			</div>
		</div>
		
		<c:import url="/WEB-INF/views/includes/footer.jsp"></c:import>
		
	</div><!-- /container -->
</body>
</html>		
		
