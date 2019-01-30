package kr.or.connect.diexam01;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//스프링 설정 클래스
public class ApplicationConfig {

	@Bean
	public Car car(Engine e) {
		Car c = new Car();
		c.setEngine(e);
		return c;
	}

	@Bean
	public Engine engine() {
		return new Engine();
	}
}