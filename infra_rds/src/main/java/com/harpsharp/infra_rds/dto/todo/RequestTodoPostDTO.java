package com.harpsharp.infra_rds.dto.todo;

import com.harpsharp.infra_rds.util.TodoStatus;

import java.time.LocalDateTime;

public record RequestTodoPostDTO(
        String title,
        String content,
        TodoStatus status,
        LocalDateTime startAt,
        LocalDateTime endAt,
        String username
) {}
