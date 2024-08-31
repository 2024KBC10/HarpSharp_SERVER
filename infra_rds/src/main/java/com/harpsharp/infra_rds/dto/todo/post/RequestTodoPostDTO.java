package com.harpsharp.infra_rds.dto.todo.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.harpsharp.infra_rds.dto.todo.TodoStatus;

import java.time.LocalDateTime;

public record RequestTodoPostDTO(
        String username,
        String title,
        String content,
        TodoStatus status,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime startAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime endAt
) {}