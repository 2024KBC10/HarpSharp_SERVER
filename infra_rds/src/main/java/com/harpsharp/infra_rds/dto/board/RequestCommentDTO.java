package com.harpsharp.infra_rds.dto.board;

public record RequestCommentDTO(
        Long postId,
        String username,
        String content,
        String memoColor,
        String pinColor) {
}
