<%@ page import = "com.kosta.jhj.dao.BoardDaoImpl" %>
<%@ page import = "com.kosta.jhj.dao.BoardDao" %>
<%@ page import = "com.kosta.jhj.vo.BoardVo" %>
<%@ page import = "java.io.IOException" %>
<%@ page import = "java.io.File" %>
<%@ page import = "java.util.ArrayList" %>
<%@ page import = "java.util.List" %>
<%@ page import = "java.io.PrintWriter" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

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
			<!-- 기본적으로 파일 첨부할 수 있는 API: input 태그의 type 속 값을 FILE로 지정, 
											  이때 form태그의 method 속성이 post, enctype 속성이 multipart/form-data -->
				<form class="board-form" method="post" action="/mysite/board?a=write" enctype="multipart/form-data">
					<input type ="hidden" name = "a" value="write">
					<table class="tbl-ex">
						<tr>
							<th colspan="2">글쓰기</th>
						</tr>
						<tr>
							<td class="label">제목</td>
							<td><input type="text" name="title" value=""></td>
						</tr>
						<tr>
							<td class="label">내용</td>
							<td>
								<textarea id="content" name="content"></textarea>
							</td>
						</tr>
						<tr>
							<td class="label">파일 첨부</td>
							<td>
								<input type="file" name="file1" value=""><br>
								<input type="file" name="file2" value=""><br>
							</td>
						</tr>
						
					</table>
					<div class="bottom">
						<a href="/mysite/board">취소</a>
						<input type="submit" value="등록">
					</div>
				</form>				
			</div>
		</div>

		<c:import url="/WEB-INF/views/includes/footer.jsp"></c:import>
		
	</div><!-- /container -->
</body>
</html>		
		
