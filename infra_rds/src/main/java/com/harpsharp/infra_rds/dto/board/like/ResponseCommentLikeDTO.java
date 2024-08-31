package com.harpsharp.infra_rds.dto.board.like;

public record ResponseCommentLikeDTO(String username, Long commentId, Long likes) {
}
