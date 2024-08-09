package com.harpsharp.infra_rds.dto.todo;

import com.harpsharp.infra_rds.util.TodoStatus;

import java.time.LocalDateTime;

public record RequestUpdateTodoPostDTO(
        String title,
        String content,
        TodoStatus status,
        LocalDateTime startAt,
        LocalDateTime endAt
) {}
