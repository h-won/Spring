package com.won.graduproject.user.db;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserDAO {

	@Autowired
	SqlSession sqlsession = null;
	
	// 아이디 중복 검사
	public int check_id(String id) throws Exception{
		return sqlsession.selectOne("user.check_id", id);
	}
	
	// 이메일 중복 검사
	public int check_email(String email) throws Exception{
		return sqlsession.selectOne("user.check_email", email);
	}
	
	// 회원가입
	@Transactional
	public int join_user(UserDTO user) throws Exception{
		return sqlsession.insert("user.join_user", user);
	}

	// 이메일 인증
	@Transactional
	public int approval_user(UserDTO user) throws Exception {
		return sqlsession.update("user.approval_user", user);
	}
	
	// 로그인 검사
	public UserDTO login(String id) throws Exception {
		return sqlsession.selectOne("user.login", id);
	}

	// 로그인 접속일자 변경
	@Transactional
	public int update_log(String id) throws Exception {
		return sqlsession.update("user.update_log", id);
	}
	
	// 아이디 찾기
	public String find_id(String email) throws Exception {
		return sqlsession.selectOne("user.find_id", email);
	}
	
	// 비밀번호 변경
	@Transactional
	public int update_pw(UserDTO user) throws Exception {
		return sqlsession.update("user.update_pw", user);
	}

	// 마이페이지
	@Transactional
	public int update_mypage(UserDTO user) throws Exception {
		return sqlsession.update("user.update_mypage", user);
	}
	
	// 회원탈퇴
	@Transactional
	public int withdrawal(UserDTO user) throws Exception {
		return sqlsession.delete("user.withdrawal", user);
	}
	
	
}