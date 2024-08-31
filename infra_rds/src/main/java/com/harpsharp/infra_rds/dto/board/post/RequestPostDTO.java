package com.harpsharp.infra_rds.dto.board.post;

public record RequestPostDTO(String username,
                             String title,
                             String content) {
}
