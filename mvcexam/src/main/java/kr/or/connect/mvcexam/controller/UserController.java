package kr.or.connect.mvcexam.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import kr.or.connect.mvcexam.dto.User;

@Controller
public class UserController {
		@RequestMapping(path="/userform", method=RequestMethod.GET)
		public String userform() {
			return "userform";
		}
		
		//dto로해서 가방처럼 한번에 받게하는 것, User객체를 만들어서
		@RequestMapping(path="/regist", method=RequestMethod.POST)
		public String regist(@ModelAttribute User user) {

			System.out.println("사용자가 입력한 user 정보입니다. 해당 정보를 이용하는 코드가 와야합니다.");
			System.out.println(user);
			return "regist";
		}
	
}