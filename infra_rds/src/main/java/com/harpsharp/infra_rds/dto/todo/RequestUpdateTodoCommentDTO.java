package com.harpsharp.infra_rds.dto.todo;

public record RequestUpdateTodoCommentDTO(
        Long commentId,
        String username,
        String content
) {}
