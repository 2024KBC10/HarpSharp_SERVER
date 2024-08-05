package com.harpsharp.infra_rds.dto.board;

import java.time.LocalDateTime;

public record ResponseCommentDTO(
        String username,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
