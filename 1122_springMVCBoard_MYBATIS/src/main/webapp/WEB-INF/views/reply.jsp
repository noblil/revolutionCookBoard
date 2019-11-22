<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>답변 달기</title>
</head>
<body>

<form action="replyInsert" method="post">

<input type="hidden" name="idx" value="${vo.idx}"/> <!-- 질문글의 글 번호 -->
<input type="hidden" name="ref" value="${vo.ref}"/> <!-- 답글 그룹 -->
<input type="hidden" name="lev" value="${vo.lev}"/> <!-- 답글 레벨 -->
<input type="hidden" name="seq" value="${vo.seq}"/> <!-- 같은 글 그룹에서 글 출력 순서 -->
<input type="hidden" name="currentPage" value="${currentPage}"/> <!-- 답글 입력 후 돌아갈 페이지 번호 -->

<!-- 질문글을 보여주는 테이블 -->
<table width="500" align="center" border="1" cellpadding="5" cellspacing="0">
	<tr><th colspan="4">질문글 보기</th></tr>
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
			<c:set var="subject" value="${fn:replace(fn:trim(vo.subject), '<', '&lt;')}"/>
			<c:set var="subject" value="${fn:replace(subject, '>', '&gt;')}"/>
			${subject}
		</td>
	</tr>
	<tr>
		<td align="center">내용</td>
		<td colspan="3">
			<c:set var="content" value="${fn:replace(fn:trim(vo.content), '<', '&lt;')}"/>
			<c:set var="content" value="${fn:replace(content, '>', '&gt;')}"/>
			<c:set var="content" value="${fn:replace(content, rn, '<br/>')}"/>
			${content}
		</td>
	</tr>
</table>
<br/>

<!-- 답글을 입력하는 테이블 -->
<table width="500" align="center" border="1" cellpadding="5" cellspacing="0">
	<tr><th colspan="2">답글 쓰기</th></tr>
	<tr>
		<td width="100" align="center">이름</td>
		<td width="400"><input type="text" name="name"/></td>
	</tr>
	<tr>
		<td align="center">제목</td>
		<td><input type="text" name="subject"/></td>
	</tr>
	<tr>
		<td align="center">내용</td>
		<td><textarea rows="10" cols="50" name="content" style="resize : none;"></textarea></td>
	</tr>
	<tr>
		<td colspan="4" align="center">
			<input type="submit" value="답글저장"/>
			<input type="reset" value="다시쓰기"/>
			<input type="button" value="돌아가기" onclick="history.back()"/>
			<input type="button" value="목록보기" onclick="location.href='list?currentPage=${currentPage}'"/>
		</td>
	</tr>
</table>
</form>

</body>
</html>







