package com.harpsharp.board;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("local")
@ComponentScan(basePackages = {"com.harpsharp.board", "com.harpsharp.infra_rds"})
class BoardApplicationTests {

	@Test
	void contextLoads() {
	}

}
