package kr.or.connect.diexam01;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("kr.or.connect.diexam01")
public class ApplicationConfig2 {
	
	//@ComponentScan어노테이션은 파라미터로 들어온 패키지 이하에서 
	//@Controller, @Service, @Repository, @Component 어노테이션이 붙어 있는 클래스를 찾아 메모리에 몽땅 올려주는 역할 

}
