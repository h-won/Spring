package com.won.graduproject.user.service;

import javax.servlet.http.HttpServletResponse;
import com.won.graduproject.user.db.UserDTO;

public interface UserService {
	
	public void check_id(String id, HttpServletResponse response) throws Exception;

	public void check_email(String email, HttpServletResponse response) throws Exception;

	public int join_user(UserDTO user, HttpServletResponse response) throws Exception;

	public String create_key() throws Exception;
	
	public void send_mail(UserDTO user, String div) throws Exception;
	
	public void approval_user(UserDTO user, HttpServletResponse response) throws Exception;
	
	public UserDTO login(UserDTO user, HttpServletResponse response) throws Exception;
	
	public void logout(HttpServletResponse response) throws Exception;
	
	public String find_id(HttpServletResponse response, String email) throws Exception;
	
	public void find_pw(HttpServletResponse response, UserDTO user) throws Exception;
	
	public UserDTO update_mypage(UserDTO user) throws Exception;
	
	public UserDTO update_pw(UserDTO user, String old_pw, HttpServletResponse response) throws Exception;
		
	public boolean withdrawal(UserDTO user, HttpServletResponse response) throws Exception;
	
	
}