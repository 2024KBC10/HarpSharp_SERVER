package com.harpsharp.infra_rds.dto.todo;

public record RequestTodoCommentDTO(
        Long postId,
        String username,
        String content
) {}
