package com.jerocaller.QuartzStudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

// TODO - 구글 트렌드 인기 검색어 일정 주기로 가져오는 스케줄링 작업.
@SpringBootApplication
@EnableFeignClients
public class QuartzStudyApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuartzStudyApplication.class, args);
	}

}
