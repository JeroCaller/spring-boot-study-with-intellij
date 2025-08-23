package com.jerocaller.SpringBootScheduling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * <p>Note</p>
 * <p>
 *     스프링부트에서 기본으로 제공하는 스케줄링 기능 사용을 위해 아래 코드와 같이
 *     {@code @EnableScheduling} 어노테이션을 <code>main()</code> 메서드를 가진
 *     클래스에 적용해야한다.
 * </p>
 */
@SpringBootApplication
@EnableScheduling
public class SpringBootSchedulingApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootSchedulingApplication.class, args);
	}

}
