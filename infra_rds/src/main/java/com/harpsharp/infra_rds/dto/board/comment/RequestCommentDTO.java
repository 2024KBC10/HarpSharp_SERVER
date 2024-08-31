package com.harpsharp.infra_rds.dto.board.comment;

public record RequestCommentDTO(
        Long postId,
        String username,
        String content,
        String memoColor,
        String pinColor) {
}
