package com.daar.indexation;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.daar.indexation.service.CVService;

@SpringBootApplication
public class IndexationApplication {

	public static void main(String[] args) {
		SpringApplication.run(IndexationApplication.class, args);
	}

	@Bean
	CommandLineRunner init(CVService cvService) {
		return (args) -> {
			cvService.init();
		};
	}
}
