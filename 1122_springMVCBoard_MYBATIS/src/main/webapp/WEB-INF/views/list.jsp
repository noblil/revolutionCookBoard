<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 목록 보기</title>

<style type="text/css">

	.button {
	  background-color: #4CAF50; /* Green */
	  border: none;
	  color: white;
	  padding: 6px 10px;
	  text-align: center;
	  text-decoration: none;
	  display: inline-block;
	  font-size: 14px;
	  margin: 2px 1px;
	  /* -webkit-transition-duration: 0.4s; */ /* Safari */
	  /* transition-duration: 0.4s; */
	  cursor: pointer;
	}
	
	.button1 {
	  background-color: white; 
	  color: black; 
	  border: 2px solid white;
	}
	
	.button1:hover {
	  background-color: white;
	  color: black;
	  border: 2px solid red;
	}
	
	.button2 {
	  background-color: white; 
	  color: black; 
	  border: 2px solid red;
	  cursor: wait;
	}
	
	span { color: red; font-weight: bold; }
	
	a:link { color: black; text-decoration: none; }
	a:visited { color: black; text-decoration: none; }
	a:hover { color: red; text-decoration: none; }
	a:active { color: lime; text-decoration: none; }
	
</style>

</head>
<body>

<table width="1000" align="center" border="1" cellpadding="5" cellspacing="0">

	<tr><th colspan="5">게시판 보기</th></tr>
	<tr>
		<td align="right" colspan="5">
			${mvcboardList.totalCount}(${mvcboardList.currentPage}/${mvcboardList.totalPage})
		</td>
	</tr>
	<tr>
		<td width="80" align="center">글번호</td>
		<td width="100" align="center">이름</td>
		<td width="620" align="center">제목</td>
		<td width="120" align="center">작성일</td>
		<td width="80" align="center">조회수</td>
	</tr>

	<!-- request 영역의 mvcboardList 객체에서 한 페이지 분량의 글이 저장된 ArrayList(mvcboardList)의 내용만 얻어낸다. -->
	<c:set var="list" value="${mvcboardList.mvcboardList}"/>

	<!-- 메인글이 한 건도 없으면 없다고 출력한다. -->
	<c:if test="${list.size() == 0}">
	<tr><td colspan="5" align="center">테이블에 저장된 글이 없습니다.</td></tr>
	</c:if>

	<!-- 메인글이 있으면 메인글의 개수 만큼 반복하며 메인글을 출력한다. -->
	<c:if test="${list.size() != 0}">
	
	<!-- 오늘 날짜를 기억하는 Date 클래스 객체를 만든다. -->
	<jsp:useBean id="date" class="java.util.Date"/>
	
	<c:forEach var="vo" items="${list}">
	<tr>
		<td align="center">${vo.idx}</td>
		<td align="center">
			<c:set var="name" value="${fn:replace(fn:trim(vo.name), '<', '&lt;')}"/>
			<c:set var="name" value="${fn:replace(name, '>', '&gt;')}"/>
			${name}
		</td>
		<td>
		
			<!-- 레벨에 따른 들어쓰기 -->
			<c:if test="${vo.lev > 0}">
				<c:forEach var="i" begin="1" end="${vo.lev}" step="1">
					&nbsp;&nbsp;&nbsp;&nbsp;
				</c:forEach>
				<img src="images/reply.png"/>
			</c:if>
			
			<!-- 오늘 입력된 글은 글 제목 앞에 NEW를 표시한다. -->
			<c:if test="${date.year == vo.writeDate.year && date.month == vo.writeDate.month && date.date == vo.writeDate.date}">
				<img src="images/new1.png"/>
			</c:if>
			<c:set var="subject" value="${fn:replace(fn:trim(vo.subject), '<', '&lt;')}"/>
			<c:set var="subject" value="${fn:replace(subject, '>', '&gt;')}"/>
			<!-- 제목에 하이퍼링크를 걸어준다. => 하이퍼링크를 클릭하면 조회수를 증가하고 메인글의 내용을 표시한다. -->
			<a href="increment?idx=${vo.idx}&currentPage=${mvcboardList.currentPage}">
				${subject}
			</a>
			<!-- 조회수(또는 댓글의 갯수)가 일정 횟수를 넘어가면 글 제목 뒤에 HOT을 표시한다. -->
			<c:if test="${vo.hit > 10}">
				<img src="images/hot.gif"/>
			</c:if>
		</td>
		<td align="center">
			<c:if test="${date.year == vo.writeDate.year && date.month == vo.writeDate.month && date.date == vo.writeDate.date}">
				<fmt:formatDate value="${vo.writeDate}" pattern="a h:mm"/>
			</c:if>
			<c:if test="${date.year != vo.writeDate.year || date.month != vo.writeDate.month || date.date != vo.writeDate.date}">
				<fmt:formatDate value="${vo.writeDate}" pattern="yyyy.MM.dd(E)"/>
			</c:if>
		</td>
		<td align="center">${vo.hit}</td>
	</tr>
	</c:forEach>
	</c:if>

	<!-- 페이지 이동 버튼 -->
	<tr>
		<td align="center" colspan="5">
		
			<!-- 처음으로, 10 페이지 앞으로 -->
			<c:if test="${mvcboardList.startPage > 1}">
				<input type="button" value="start page" onclick="location.href='?currentPage=1'" title="첫 페이지 이동합니다."/>
				<input type="button" value="-10 page" onclick="location.href='?currentPage=${mvcboardList.startPage - 1}'" 
						title="이전 10 페이지로 이동합니다."/>
			</c:if>
			<c:if test="${mvcboardList.startPage <= 1}">
				<input type="button" value="start page" disabled="disabled" title="이미 첫 페이지 입니다."/>
				<input type="button" value="-10 page" disabled="disabled" title="이전 10 페이지가 없습니다."/>
			</c:if>
			
			<!-- 1 페이지 앞으로 -->
			<c:if test="${mvcboardList.currentPage > 1}">
				<input type="button" value="-1 page" onclick="location.href='?currentPage=${mvcboardList.currentPage - 1}'" 
						title="이전 페이지로 이동합니다."/>
			</c:if>
			<c:if test="${mvcboardList.currentPage <= 1}">
				<input type="button" value="-1 page" disabled="disabled" title="이전 페이지가 없습니다."/>
			</c:if>
			
			<!-- 10 페이지 단위로 표시되는 페이지 이동 버튼 -->
			<c:forEach var="i" begin="${mvcboardList.startPage}" end="${mvcboardList.endPage}" step="1">
				<c:if test="${mvcboardList.currentPage == i}">
					<input class="button button2" type="button" value="${i}" disabled="disabled"/>
				</c:if>
				<c:if test="${mvcboardList.currentPage != i}">
					<input class="button button1" type="button" value="${i}" onclick="location.href='?currentPage=${i}'"/>
				</c:if>
			</c:forEach>
			
			<!-- 1 페이지 뒤로 -->
			<c:if test="${mvcboardList.currentPage < mvcboardList.totalPage}">
				<input type="button" value="+1 page" onclick="location.href='?currentPage=${mvcboardList.currentPage + 1}'" 
						title="다음 페이지로 이동합니다."/>
			</c:if>
			<c:if test="${mvcboardList.currentPage >= mvcboardList.totalPage}">
				<input type="button" value="+1 page" disabled="disabled" title="다음 페이지가 없습니다."/>
			</c:if>
			
			<!-- 마지막으로, 10 페이지 뒤로 -->
			<c:if test="${mvcboardList.endPage < mvcboardList.totalPage}">
				<input type="button" value="+10 page" onclick="location.href='?currentPage=${mvcboardList.endPage + 1}'" 
						title="다음 10 페이지로 이동합니다."/>
				<input type="button" value="end page" onclick="location.href='?currentPage=${mvcboardList.totalPage}'" 
						title="마지막 페이지로 이동합니다."/>
			</c:if>
			<c:if test="${mvcboardList.endPage >= mvcboardList.totalPage}">
				<input type="button" value="+10 page" disabled="disabled" title="다음 10 페이지가 없습니다."/>
				<input type="button" value="end page" disabled="disabled" title="이미 마지막 페이지 입니다."/>
			</c:if>
			
		</td>
	</tr>
	
	<!-- 글쓰기 페이지로 이동하는 버튼 -->
	<tr>
		<td align="right" colspan="5">
			<input type="button" value="글쓰기" onclick="location.href='insert.nhn'"/>
		</td>
	</tr>

</table>

</body>
</html>
















