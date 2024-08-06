package com.harpsharp.infra_rds.dto.board;

public record RequestPostDTO(String username,
                             String title,
                             String content) {
}
