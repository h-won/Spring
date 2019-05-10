<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script>
	$(function() {
		$("#find_id_btn").click(function() {
			location.href = '../user/find_id_form.do';
		});
		$("#find_pw_btn").click(function() {
			location.href = '../user/find_pw_form.do';
		});
	})
</script>
<title>Login Form</title>
</head>
<body>
	<div class="w3-content w3-container w3-margin-top">
		<div class="w3-container w3-card-4">
			<div class="w3-center w3-large w3-margin-top">
				<h3>Log In</h3>
			</div>
			<div>
				<form action="../user/login.do" method="post">
					<p>
						<label>ID</label>
						<span class="w3-right w3-button w3-hover-white" title="아이디 찾기" id="find_id_btn">
							<i class="fa fa-exclamation-triangle w3-hover-text-red w3-large"></i>
						</span>
						<input class="w3-input" id="id" name="id" type="text" required>
					</p>
					<p>
						<label>Password</label>
						<span class="w3-right w3-button w3-hover-white" title="비밀번호 찾기" id="find_pw_btn">
							<i class="fa fa-exclamation-triangle w3-hover-text-red w3-large"></i>
						</span>
						<input class="w3-input" id="pw" name="pw" type="password" required>
					</p>
					<p class="w3-center">
						<button type="submit"
							class="w3-button w3-block w3-black w3-ripple w3-margin-top w3-round">Log
							in</button>



						<!-- 네이버 로그인 화면으로 이동 시키는 URL -->
						<!-- 네이버 로그인 화면에서 ID, PW를 올바르게 입력하면 callback 메소드 실행 요청 -->
					<div id="naver_id_login" style="text-align: center">
						<a href="${url}">
		<!-- <img width="223" src="${pageContext.request.contextPath}/resources/img/naver_Bn_Green.PNG"/> -->

		<img width="223" src="https://developers.naver.com/doc/review_201802/CK_bEFnWMeEBjXpQ5o8N_20180202_7aot50.png" />
		</a>
	</div>
						
						
		<a href="${google_url}">구글 로그인
		<!-- 
		<button id="btnJoinGoogle" class="btn btn-primary btn-round"
                                style="width: 100%">
                                <i class="fa fa-google" aria-hidden="true"></i>Google Login
                            </button>
                             -->
                            </a> 
						
						
						
						
						<button type="button"
							class="w3-button w3-block w3-black w3-ripple w3-margin-top w3-margin-bottom w3-round"
							onclick="history.go(-1)">Cancel</button>
					</p>
					
				</form>
			</div>
		</div>
	</div>
</body>
</html>