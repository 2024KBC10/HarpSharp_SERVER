package com.harpsharp.infra_rds.dto.board.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.harpsharp.infra_rds.dto.board.comment.ResponseCommentDTO;

import java.time.LocalDateTime;
import java.util.Map;

public record ResponsePostDTO(
        String username,
        String title,
        String content,
        Long likes,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime createdAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime updatedAt,
        Map<Long, ResponseCommentDTO> comments)
{}