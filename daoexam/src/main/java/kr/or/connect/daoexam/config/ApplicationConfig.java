package kr.or.connect.daoexam.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

//실행이 되면서 여기 있는 설정들에 대한 정보를 읽어들이게 하는 것
//하나의 클래스가 모든 정보를 가지고 있으면 처리가 어려우니까 Import 사용해서 설정 파일을 여러 개로 나눠서 작성
@Configuration
@ComponentScan(basePackages= {"kr.or.connect.daoexam.dao"})
@Import({DBConfig.class})
public class ApplicationConfig {

}