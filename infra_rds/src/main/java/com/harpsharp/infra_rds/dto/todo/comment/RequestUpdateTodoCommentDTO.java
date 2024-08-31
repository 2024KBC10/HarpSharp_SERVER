package com.harpsharp.infra_rds.dto.todo.comment;

public record RequestUpdateTodoCommentDTO(
        Long commentId,
        String username,
        String content
) {}
