package com.harpsharp.board;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

@SpringBootTest
@ComponentScan(basePackages = {"com.harpsharp.board", "com.harpsharp.infra_rds"})
class BoardApplicationTests {

	@Test
	void contextLoads() {
	}

}
