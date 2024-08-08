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

	// GET board/posts
	// POST board/posts
	// GET board/posts/{postId}
	// PATCH board/posts/{postId}
	// DELETE board/posts/{postId}

	// GET    board/posts/{postId}/comments
	// POST   board/posts/{postId}/comments
	// GET 	  board/posts/{postId}/comments/{commentId}
	// PATCH  board/posts/{postId}/comments/{commentId}
	// DELETE board/posts/{postId}/comments/{commentId}

}
