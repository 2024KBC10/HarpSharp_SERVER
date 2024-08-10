package com.harpsharp.infra_rds.dto.todo;

public record RequestTodoCommentDTO(
        Long todoPostId,
        String username,
        String content
) {}
