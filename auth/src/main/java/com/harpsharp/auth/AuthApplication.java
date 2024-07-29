package com.harpsharp.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication(scanBasePackages = "com.harpsharp.infra_rds")
public class AuthApplication {
	public static void main(String[] args) {
		System.setProperty("spring.profiles.default", "local");
		SpringApplication.run(AuthApplication.class, args);
	}
}
