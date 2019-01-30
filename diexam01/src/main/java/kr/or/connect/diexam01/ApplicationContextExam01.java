package kr.or.connect.diexam01;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ApplicationContextExam01 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		//ApplicationContext는 인터페이스, ApplicationContext을 구현하는 다양한 컨테이너가 존재한다
		//그중에서 이런 xml 파일을 classpath에서 읽어들여서 사용하는 객체가 ClassPathXmlApplicationContext
		ApplicationContext ac=new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		System.out.println("초기화 완료!!!");
		
		UserBean userBean=(UserBean)ac.getBean("userBean");
		userBean.setName("kang");
		
		System.out.println(userBean.getName());

		UserBean userBean2=(UserBean)ac.getBean("userBean");

		if(userBean == userBean2)
			System.out.println("같은 인스턴스 입니다..");
		//사용자가 계속 getBean()해서 요청을 해도 객체를 계속 만들어내는 것이 아닌 하나 만든 bean 이용
		
	}

}
