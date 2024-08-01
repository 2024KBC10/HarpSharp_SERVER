package com.harpsharp.todo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("local")
@ComponentScan(basePackages = {"com.harpsharp.todo", "com.harpsharp.infra_rds"})
class TodoApplicationTests {

	@Test
	void contextLoads() {
	}

}
