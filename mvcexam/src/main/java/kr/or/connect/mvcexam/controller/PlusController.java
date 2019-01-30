package kr.or.connect.mvcexam.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

//나는 컨트롤러에요라는 의미의 어노테이션을 붙여줘야 이거를 찾아서 사용
@Controller  
public class PlusController {

	@GetMapping(path = "/plusform")
	public String plusform() {
		return "plusForm"; //뷰에 정보만 넘겨주는 거니까 
	}
	
	
	/*RequestParam에서 name이 value1로 들어오는 것은 plus 메서드 내에서 int타입의 value1로 사용한다는 의미
	 * plusForm.jsp의 input에서 name이 value1인 것이 넘어오는 게 RequestParam */
	@PostMapping(path = "/plus")
	public String plus(@RequestParam(name = "value1", required = true) int value1,
			@RequestParam(name = "value2", required = true) int value2, ModelMap modelMap) {
		int result = value1 + value2;

		modelMap.addAttribute("value1", value1);
		modelMap.addAttribute("value2", value2);
		modelMap.addAttribute("result", result);
		return "plusResult";
	}

	
	
}