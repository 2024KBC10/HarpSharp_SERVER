package com.harpsharp.infra_rds.dto.todo;

public record RequestUpdateTodoCommentDTO(
        String username,
        String content
) {}
