package com.harpsharp.infra_rds.dto.todo.comment;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.harpsharp.infra_rds.dto.todo.TodoStatus;

import java.time.LocalDateTime;

public record RequestUpdateTodoPostDTO(
        Long postId,
        String username,
        String title,
        String content,
        TodoStatus status,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime startAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime endAt
) {}
