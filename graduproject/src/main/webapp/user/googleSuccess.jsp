<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!doctype html>
<html lang="ko">
<head>
<script type="text/javascript"
	src="http://code.jquery.com/jquery-1.11.3.min.js"></script>
<style type="text/css">
html, div, body, h3 {
	margin: 0;
	padding: 0;
}

h3 {
	display: inline-block;
	padding: 0.6em;
}
</style>

</head>

<body>
<div
		style="background-color: #15a181; width: 100%; height: 50px; text-align: center; color: white;">
		<h3>joiin</h3>
	</div>
	이름
	<c:out value="${resultgoo.name}"/>
	<br>
	이메일
	<c:out value="${resultgoo.email}"/>
	
 
	
	
	<br>
	<h2 style="text-align: center" id="name"></h2>
	<h4 style="text-align: center" id="email"></h4>
</body>
</html>