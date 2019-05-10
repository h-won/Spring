package com.won.graduproject.user.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.social.google.connect.GoogleOAuth2Template;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.won.graduproject.user.db.UserDTO;
import com.won.graduproject.user.naver.NaverLoginBO;
import com.won.graduproject.user.service.UserServiceImpl;

@Controller
@RequestMapping("/user/*")
public class UserController {

	// ȸ�� ���� �� �̵�
	@RequestMapping(value = "/userJoinForm.do")
	public String userJoinForm() throws Exception {
		return "/user/userJoinForm";
	}

	@Autowired
	private UserServiceImpl service;

	// ���̵� �ߺ� �˻�(AJAX)
	@RequestMapping(value = "/check_id.do", method = RequestMethod.POST)
	public void check_id(@RequestParam("id") String id, HttpServletResponse response) throws Exception {
		service.check_id(id, response);
	}

	// �̸��� �ߺ� �˻�(AJAX)
	@RequestMapping(value = "/check_email.do", method = RequestMethod.POST)
	public void check_email(@RequestParam("email") String email, HttpServletResponse response) throws Exception {
		service.check_email(email, response);
	}

	// ȸ�� ����
	@RequestMapping(value = "/join_user.do", method = RequestMethod.POST)
	public String join_user(@ModelAttribute UserDTO user, RedirectAttributes rttr, HttpServletResponse response)
			throws Exception {
		rttr.addFlashAttribute("result", service.join_user(user, response));
		// return "redircet:./userJoinForm.do";
		return "index";
	}
	
	// ȸ�� ����
	@RequestMapping(value = "/approval_user.do", method = RequestMethod.POST)
	public void approval_user(@ModelAttribute UserDTO user, HttpServletResponse response) throws Exception {
		service.approval_user(user, response);
	}
	
//	// �α��� �� �̵�
//	@RequestMapping(value = "/login_form.do", method = RequestMethod.GET)
//	public String login_form() throws Exception {
//		return "/user/loginForm";
//	}
	
	
	/* NaverLoginBO */
	private NaverLoginBO naverLoginBO;
	private String apiResult = null;

	@Autowired
	private void setNaverLoginBO(NaverLoginBO naverLoginBO) {
		this.naverLoginBO = naverLoginBO;
	}
	
	@Autowired
    private GoogleOAuth2Template googleOAuth2Template;
    
    @Autowired
    private OAuth2Parameters googleOAuth2Parameters;
	
	//�α��� �� �̵�
		@RequestMapping(value = "/login_form.do", method = RequestMethod.GET)
		public String login_form(Model model, HttpSession session) throws Exception {
			
			/* ���̹����̵�� ���� URL�� �����ϱ� ���Ͽ� naverLoginBOŬ������ getAuthorizationUrl�޼ҵ� ȣ�� */
			String naverAuthUrl = naverLoginBO.getAuthorizationUrl(session);

			
			System.out.println("���̹�:" + naverAuthUrl);

			// ���̹�
			model.addAttribute("url", naverAuthUrl);
			
			//���� URL
	        String url = googleOAuth2Template.buildAuthenticateUrl(GrantType.AUTHORIZATION_CODE, googleOAuth2Parameters);
	        System.out.println("googleLogin, url : " + url);
	        model.addAttribute("google_url", url);
			
			return "/user/loginForm";
		}
	
		// ���̹� �α��� ������ callbackȣ�� �޼ҵ�
		@RequestMapping(value = "/naver_callback.do", method = { RequestMethod.GET, RequestMethod.POST })
		public String callback(Model model, @RequestParam String code, @RequestParam String state, HttpSession session)
				throws IOException {
			System.out.println("����� callback");
			OAuth2AccessToken oauthToken;
			oauthToken = naverLoginBO.getAccessToken(session, code, state);
			// �α��� ����� ������ �о�´�.
			apiResult = naverLoginBO.getUserProfile(oauthToken);
			model.addAttribute("result", apiResult);
			System.out.println(apiResult);

			/* ���̹� �α��� ���� ������ View ȣ�� */
			return "/user/naverSuccess";
			//return "index";
		}
		
		// ���� �α��� ������ callbackȣ�� �޼ҵ�
		@RequestMapping(value = "/google_callback.do" , method = { RequestMethod.GET, RequestMethod.POST })
	    public String google_callback(HttpServletRequest request,Model model) throws Exception {
	 
	        String code = request.getParameter("code");
	        System.out.println(code);
	        
	        //RestTemplate�� ����Ͽ� Access Token �� profile�� ��û�Ѵ�.
	        RestTemplate restTemplate = new RestTemplate();
	        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
	        parameters.add("code", code);
	        parameters.add("client_id", google_key.client_id);
	        parameters.add("client_secret", google_key.client_secret); 
	        parameters.add("redirect_uri", googleOAuth2Parameters.getRedirectUri());
	        parameters.add("grant_type", "authorization_code");
	 
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
	        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(parameters, headers);
	        ResponseEntity<Map> responseEntity = restTemplate.exchange(google_key.google_url, HttpMethod.POST, requestEntity, Map.class);
	        Map<String, Object> responseMap = responseEntity.getBody();
	 
	        // id_token ��� Ű�� ����ڰ� ������ �����Ѵ�.
	        // �޾ƿ� ����� JWT (Json Web Token) �������� �޾ƿ´�. �޸� ������ ��� ù ��°�� �� ��ū�� ���� ��Ÿ ����, �� ��°�� �츮�� �ʿ��� ������ �����Ѵ�.
	        // ����° �κп��� �������� �����ϱ� ���� Ư�� �˰������� ��ȣȭ�Ǿ� ���̴׿� ����Ѵ�.
	        //Base 64�� ���ڵ� �Ǿ� �����Ƿ� ���ڵ��Ѵ�.
	 
	        String[] tokens = ((String)responseMap.get("id_token")).split("\\.");
	        Base64 base64 = new Base64(true);
	        String body = new String(base64.decode(tokens[1]));
	        
	        System.out.println(tokens.length);
	        System.out.println(new String(Base64.decodeBase64(tokens[0]), "utf-8"));
	        System.out.println(new String(Base64.decodeBase64(tokens[1]), "utf-8"));
	 
	        //Jackson�� ����� JSON�� �ڹ� Map �������� ��ȯ
	        ObjectMapper mapper = new ObjectMapper();
	        Map<String, String> result = mapper.readValue(body, Map.class);
	     
	        String bodygoogle=new String(Base64.decodeBase64(tokens[1]),"utf-8");
	        System.out.println(bodygoogle);
	        ObjectMapper mappergoo = new ObjectMapper();
	        Map<String, String> resultgoo = mappergoo.readValue(bodygoogle, Map.class);
	        model.addAttribute("resultgoo", resultgoo);
	        System.out.println(resultgoo.get("email"));
	        System.out.println(resultgoo.get("name"));
	        
	        /* ���� �α��� ���� ������ View ȣ�� */
			return "/user/googleSuccess";
	 
	    }
		
	// �Ϲ� �α���
	@RequestMapping(value = "/login.do", method = {RequestMethod.POST , RequestMethod.GET} )
	public String login(@ModelAttribute UserDTO user, HttpSession session, HttpServletResponse response)
			throws Exception {
		user = service.login(user, response);
		session.setAttribute("user", user);
		return "index";
	}

	// �α׾ƿ�
	@RequestMapping(value = "/logout.do", method = RequestMethod.GET)
	public void logout(HttpSession session, HttpServletResponse response) throws Exception {
		session.invalidate();
//			session.removeAttribute("member");
		service.logout(response);
	}
	
	// ���̵� ã�� ��
	@RequestMapping(value = "/find_id_form.do")
	public String find_id_form() throws Exception {
		return "/user/find_id_form";
	}
	
	// ���̵� ã��
	@RequestMapping(value = "/find_id.do", method = RequestMethod.POST)
	public String find_id(HttpServletResponse response, @RequestParam("email") String email, Model md)
			throws Exception {
		md.addAttribute("id", service.find_id(response, email));
		return "/user/find_id";
	}
	
	// ��й�ȣ ã�� ��
	@RequestMapping(value = "/find_pw_form.do")
	public String find_pw_form() throws Exception {
		return "/user/find_pw_form";
	}
	
	// ��й�ȣ ã��
	@RequestMapping(value = "/find_pw.do", method = RequestMethod.POST)
	public void find_pw(@ModelAttribute UserDTO user, HttpServletResponse response) throws Exception {
		service.find_pw(response, user);
	}

	// ���������� �̵�
	@RequestMapping(value = "/mypage.do")
	public String mypage() throws Exception {
		return "/user/mypage";
	}

	// ���������� ����
	@RequestMapping(value = "/update_mypage.do", method = RequestMethod.POST)
	public String update_mypage(@ModelAttribute UserDTO user, HttpSession session, RedirectAttributes rttr)
			throws Exception {
		session.setAttribute("user", service.update_mypage(user));
		rttr.addFlashAttribute("msg", "ȸ������ ���� �Ϸ�");
		return "redirect:/user/mypage.do";
	}

	// ��й�ȣ ����
	@RequestMapping(value = "/update_pw.do", method = RequestMethod.POST)
	public String update_pw(@ModelAttribute UserDTO user, @RequestParam("old_pw") String old_pw, HttpSession session,
			HttpServletResponse response, RedirectAttributes rttr) throws Exception {
		session.setAttribute("user", service.update_pw(user, old_pw, response));
		rttr.addFlashAttribute("msg", "��й�ȣ ���� �Ϸ�");
		return "redirect:/user/mypage.do";
	}	
		
	// ȸ��Ż��
	@RequestMapping(value = "/withdrawal.do", method = RequestMethod.POST)
	public String withdrawal_form(@ModelAttribute UserDTO user, HttpSession session, HttpServletResponse response)
			throws Exception {
		if (service.withdrawal(user, response)) {
			session.invalidate();
		}
		// return "redirect:/index.do";
		return "index";
	}
	
	
	
	
	

}