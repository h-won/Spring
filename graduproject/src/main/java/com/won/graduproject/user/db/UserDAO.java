package com.won.graduproject.user.db;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserDAO {

	@Autowired
	SqlSession sqlsession = null;
	
	// ���̵� �ߺ� �˻�
	public int check_id(String id) throws Exception{
		return sqlsession.selectOne("user.check_id", id);
	}
	
	// �̸��� �ߺ� �˻�
	public int check_email(String email) throws Exception{
		return sqlsession.selectOne("user.check_email", email);
	}
	
	// ȸ������
	@Transactional
	public int join_user(UserDTO user) throws Exception{
		return sqlsession.insert("user.join_user", user);
	}

	// �̸��� ����
	@Transactional
	public int approval_user(UserDTO user) throws Exception {
		return sqlsession.update("user.approval_user", user);
	}
	
	// �α��� �˻�
	public UserDTO login(String id) throws Exception {
		return sqlsession.selectOne("user.login", id);
	}

	// �α��� �������� ����
	@Transactional
	public int update_log(String id) throws Exception {
		return sqlsession.update("user.update_log", id);
	}
	
	// ���̵� ã��
	public String find_id(String email) throws Exception {
		return sqlsession.selectOne("user.find_id", email);
	}
	
	// ��й�ȣ ����
	@Transactional
	public int update_pw(UserDTO user) throws Exception {
		return sqlsession.update("user.update_pw", user);
	}

	// ����������
	@Transactional
	public int update_mypage(UserDTO user) throws Exception {
		return sqlsession.update("user.update_mypage", user);
	}
	
	// ȸ��Ż��
	@Transactional
	public int withdrawal(UserDTO user) throws Exception {
		return sqlsession.delete("user.withdrawal", user);
	}
	
	
}