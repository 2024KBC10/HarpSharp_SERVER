package com.harpsharp.infra_rds.dto.todo;

public record RequestTodoCommentDTO(
        String content,
        Long todoPostId
) {}
