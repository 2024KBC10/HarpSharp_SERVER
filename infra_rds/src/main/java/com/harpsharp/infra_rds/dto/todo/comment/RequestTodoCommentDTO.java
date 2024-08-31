package com.harpsharp.infra_rds.dto.todo.comment;

public record RequestTodoCommentDTO(
        Long postId,
        String username,
        String content
) {}
