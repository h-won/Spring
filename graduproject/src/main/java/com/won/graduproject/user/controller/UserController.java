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

	// 회원 가입 폼 이동
	@RequestMapping(value = "/userJoinForm.do")
	public String userJoinForm() throws Exception {
		return "/user/userJoinForm";
	}

	@Autowired
	private UserServiceImpl service;

	// 아이디 중복 검사(AJAX)
	@RequestMapping(value = "/check_id.do", method = RequestMethod.POST)
	public void check_id(@RequestParam("id") String id, HttpServletResponse response) throws Exception {
		service.check_id(id, response);
	}

	// 이메일 중복 검사(AJAX)
	@RequestMapping(value = "/check_email.do", method = RequestMethod.POST)
	public void check_email(@RequestParam("email") String email, HttpServletResponse response) throws Exception {
		service.check_email(email, response);
	}

	// 회원 가입
	@RequestMapping(value = "/join_user.do", method = RequestMethod.POST)
	public String join_user(@ModelAttribute UserDTO user, RedirectAttributes rttr, HttpServletResponse response)
			throws Exception {
		rttr.addFlashAttribute("result", service.join_user(user, response));
		// return "redircet:./userJoinForm.do";
		return "index";
	}
	
	// 회원 인증
	@RequestMapping(value = "/approval_user.do", method = RequestMethod.POST)
	public void approval_user(@ModelAttribute UserDTO user, HttpServletResponse response) throws Exception {
		service.approval_user(user, response);
	}
	
//	// 로그인 폼 이동
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
	
	//로그인 폼 이동
		@RequestMapping(value = "/login_form.do", method = RequestMethod.GET)
		public String login_form(Model model, HttpSession session) throws Exception {
			
			/* 네이버아이디로 인증 URL을 생성하기 위하여 naverLoginBO클래스의 getAuthorizationUrl메소드 호출 */
			String naverAuthUrl = naverLoginBO.getAuthorizationUrl(session);

			
			System.out.println("네이버:" + naverAuthUrl);

			// 네이버
			model.addAttribute("url", naverAuthUrl);
			
			//구글 URL
	        String url = googleOAuth2Template.buildAuthenticateUrl(GrantType.AUTHORIZATION_CODE, googleOAuth2Parameters);
	        System.out.println("googleLogin, url : " + url);
	        model.addAttribute("google_url", url);
			
			return "/user/loginForm";
		}
	
		// 네이버 로그인 성공시 callback호출 메소드
		@RequestMapping(value = "/naver_callback.do", method = { RequestMethod.GET, RequestMethod.POST })
		public String callback(Model model, @RequestParam String code, @RequestParam String state, HttpSession session)
				throws IOException {
			System.out.println("여기는 callback");
			OAuth2AccessToken oauthToken;
			oauthToken = naverLoginBO.getAccessToken(session, code, state);
			// 로그인 사용자 정보를 읽어온다.
			apiResult = naverLoginBO.getUserProfile(oauthToken);
			model.addAttribute("result", apiResult);
			System.out.println(apiResult);

			/* 네이버 로그인 성공 페이지 View 호출 */
			return "/user/naverSuccess";
			//return "index";
		}
		
		// 구글 로그인 성공시 callback호출 메소드
		@RequestMapping(value = "/google_callback.do" , method = { RequestMethod.GET, RequestMethod.POST })
	    public String google_callback(HttpServletRequest request,Model model) throws Exception {
	 
	        String code = request.getParameter("code");
	        System.out.println(code);
	        
	        //RestTemplate을 사용하여 Access Token 및 profile을 요청한다.
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
	 
	        // id_token 라는 키에 사용자가 정보가 존재한다.
	        // 받아온 결과는 JWT (Json Web Token) 형식으로 받아온다. 콤마 단위로 끊어서 첫 번째는 현 토큰에 대한 메타 정보, 두 번째는 우리가 필요한 내용이 존재한다.
	        // 세번째 부분에는 위변조를 방지하기 위한 특정 알고리즘으로 암호화되어 사이닝에 사용한다.
	        //Base 64로 인코딩 되어 있으므로 디코딩한다.
	 
	        String[] tokens = ((String)responseMap.get("id_token")).split("\\.");
	        Base64 base64 = new Base64(true);
	        String body = new String(base64.decode(tokens[1]));
	        
	        System.out.println(tokens.length);
	        System.out.println(new String(Base64.decodeBase64(tokens[0]), "utf-8"));
	        System.out.println(new String(Base64.decodeBase64(tokens[1]), "utf-8"));
	 
	        //Jackson을 사용한 JSON을 자바 Map 형식으로 변환
	        ObjectMapper mapper = new ObjectMapper();
	        Map<String, String> result = mapper.readValue(body, Map.class);
	     
	        String bodygoogle=new String(Base64.decodeBase64(tokens[1]),"utf-8");
	        System.out.println(bodygoogle);
	        ObjectMapper mappergoo = new ObjectMapper();
	        Map<String, String> resultgoo = mappergoo.readValue(bodygoogle, Map.class);
	        model.addAttribute("resultgoo", resultgoo);
	        System.out.println(resultgoo.get("email"));
	        System.out.println(resultgoo.get("name"));
	        
	        /* 구글 로그인 성공 페이지 View 호출 */
			return "/user/googleSuccess";
	 
	    }
		
	// 일반 로그인
	@RequestMapping(value = "/login.do", method = {RequestMethod.POST , RequestMethod.GET} )
	public String login(@ModelAttribute UserDTO user, HttpSession session, HttpServletResponse response)
			throws Exception {
		user = service.login(user, response);
		session.setAttribute("user", user);
		return "index";
	}

	// 로그아웃
	@RequestMapping(value = "/logout.do", method = RequestMethod.GET)
	public void logout(HttpSession session, HttpServletResponse response) throws Exception {
		session.invalidate();
//			session.removeAttribute("member");
		service.logout(response);
	}
	
	// 아이디 찾기 폼
	@RequestMapping(value = "/find_id_form.do")
	public String find_id_form() throws Exception {
		return "/user/find_id_form";
	}
	
	// 아이디 찾기
	@RequestMapping(value = "/find_id.do", method = RequestMethod.POST)
	public String find_id(HttpServletResponse response, @RequestParam("email") String email, Model md)
			throws Exception {
		md.addAttribute("id", service.find_id(response, email));
		return "/user/find_id";
	}
	
	// 비밀번호 찾기 폼
	@RequestMapping(value = "/find_pw_form.do")
	public String find_pw_form() throws Exception {
		return "/user/find_pw_form";
	}
	
	// 비밀번호 찾기
	@RequestMapping(value = "/find_pw.do", method = RequestMethod.POST)
	public void find_pw(@ModelAttribute UserDTO user, HttpServletResponse response) throws Exception {
		service.find_pw(response, user);
	}

	// 마이페이지 이동
	@RequestMapping(value = "/mypage.do")
	public String mypage() throws Exception {
		return "/user/mypage";
	}

	// 마이페이지 수정
	@RequestMapping(value = "/update_mypage.do", method = RequestMethod.POST)
	public String update_mypage(@ModelAttribute UserDTO user, HttpSession session, RedirectAttributes rttr)
			throws Exception {
		session.setAttribute("user", service.update_mypage(user));
		rttr.addFlashAttribute("msg", "회원정보 수정 완료");
		return "redirect:/user/mypage.do";
	}

	// 비밀번호 변경
	@RequestMapping(value = "/update_pw.do", method = RequestMethod.POST)
	public String update_pw(@ModelAttribute UserDTO user, @RequestParam("old_pw") String old_pw, HttpSession session,
			HttpServletResponse response, RedirectAttributes rttr) throws Exception {
		session.setAttribute("user", service.update_pw(user, old_pw, response));
		rttr.addFlashAttribute("msg", "비밀번호 수정 완료");
		return "redirect:/user/mypage.do";
	}	
		
	// 회원탈퇴
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