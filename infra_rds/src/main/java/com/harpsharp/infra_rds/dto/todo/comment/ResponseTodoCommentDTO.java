package com.harpsharp.infra_rds.dto.todo.comment;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ResponseTodoCommentDTO(
        String username,
        String content,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime createdAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime updatedAt
) {}
