<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
</head>
<body>

	<input type="button" value="ȸ������"
		onclick="location.href='./user/userJoinForm.do'">
		

	<c:if test="${user == null}">
		<input type="button" value="�α���"
			onclick="location.href='./user/login_form.do'">
	</c:if>
	<c:if test="${user != null}">
	<a href="./mypage.do">����������(${user.id })</a>
		<input type="button" value="�α׾ƿ�"
			onclick="location.href='./logout.do'">
	</c:if>		

</body>
</html>