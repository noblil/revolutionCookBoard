<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 보기</title>
</head>
<body>

<form action="update" method="post">

<input type="hidden" name="idx" value="${vo.idx}"/>
<input type="hidden" name="currentPage" value="${currentPage}"/>

<table width="500" align="center" border="1" cellpadding="5" cellspacing="0">
	<tr><th colspan="4">게시글 보기</th></tr>
	<tr>
		<td width="80" align="center">글번호</td>
		<td width="140" align="center">작성자</td>
		<td width="200" align="center">작성일</td>
		<td width="80" align="center">조회수</td>
	</tr>
	<tr>
		<td align="center">${vo.idx}</td>
		<td align="center">
			<c:set var="name" value="${fn:replace(fn:trim(vo.name), '<', '&lt;')}"/>
			<c:set var="name" value="${fn:replace(name, '>', '&gt;')}"/>
			${name}
		</td>
		<td align="center">
			<fmt:formatDate value="${vo.writeDate}" pattern="yyyy/MM/dd(E) HH:mm"/>
		</td>
		<td align="center">${vo.hit}</td>
	</tr>
	<tr>
		<td align="center">제목</td>
		<td colspan="3">
			<%--
			<c:set var="subject" value="${fn:replace(fn:trim(vo.subject), '<', '&lt;')}"/>
			<c:set var="subject" value="${fn:replace(subject, '>', '&gt;')}"/>
			${subject}
			--%>
			<input type="text" name="subject" value="${vo.subject}" size="40"/>
		</td>
	</tr>
	<tr>
		<td align="center">내용</td>
		<td colspan="3">
			<%--
			<c:set var="content" value="${fn:replace(fn:trim(vo.content), '<', '&lt;')}"/>
			<c:set var="content" value="${fn:replace(content, '>', '&gt;')}"/>
			<c:set var="content" value="${fn:replace(content, rn, '<br/>')}"/>
			${content}
			--%>
			<textarea rows="10" cols="50" name="content" style="resize : none;">${vo.content}</textarea>
		</td>
	</tr>
	<tr>
		<td colspan="4" align="center">
			<input type="submit" value="수정하기"/>
			<input type="button" value="삭제하기" onclick="location.href='delete?idx=${vo.idx}&currentPage=${currentPage}'"/>
			<input type="button" value="답변달기" onclick="location.href='reply?idx=${vo.idx}&currentPage=${currentPage}'"/>
			<input type="button" value="돌아가기" onclick="location.href='list?currentPage=${currentPage}'"/>
		</td>
	</tr>
</table>
</form>

</body>
</html>







