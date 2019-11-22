<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<%
//	테이블에 메인글을 저장했으므로 한 페이지 분량의 글을 얻어오는 페이지를 컨트롤러에 요청한다.
	response.sendRedirect("list");
%>

</body>
</html>