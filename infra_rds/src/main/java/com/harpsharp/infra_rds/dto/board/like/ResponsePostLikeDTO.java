package com.harpsharp.infra_rds.dto.board.like;

public record ResponsePostLikeDTO(String username, Long postId, Long likes) {
}
