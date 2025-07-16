package com.ms.email;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MicroserviceEmailApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceEmailApplication.class, args);
	}

}
