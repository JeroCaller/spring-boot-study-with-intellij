package com.jerocaller.QuartzStudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

// TODO - 총 3개의 job들을 스케줄링하기.
@SpringBootApplication
@EnableFeignClients
public class QuartzStudyApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuartzStudyApplication.class, args);
	}

}
