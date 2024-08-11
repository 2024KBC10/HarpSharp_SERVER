package com.harpsharp.infra_rds.dto.todo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.harpsharp.infra_rds.util.TodoStatus;

import java.time.LocalDateTime;

public record RequestUpdateTodoPostDTO(
        Long postId,
        String username,
        String title,
        String content,
        String content_hint,
        String content_goal,
        TodoStatus status,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime startAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime endAt
) {}
