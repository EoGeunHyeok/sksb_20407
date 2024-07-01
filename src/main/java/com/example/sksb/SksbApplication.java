package com.example.sksb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SksbApplication {

	// 메인 페이지
	public static void main(String[] args) {
		SpringApplication.run(SksbApplication.class, args);
	}

}
