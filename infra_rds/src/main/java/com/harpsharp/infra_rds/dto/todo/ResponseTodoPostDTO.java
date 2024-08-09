package com.harpsharp.infra_rds.dto.todo;

import com.harpsharp.infra_rds.util.TodoStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public record ResponseTodoPostDTO(
        Long id,
        String title,
        String content,
        TodoStatus status,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime startAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime endAt,
        Long likes,
        List<ResponseTodoCommentDTO> comments
) {}
