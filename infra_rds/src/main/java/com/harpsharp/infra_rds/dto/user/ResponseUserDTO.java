package com.harpsharp.infra_rds.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.harpsharp.infra_rds.dto.board.ResponseCommentDTO;
import com.harpsharp.infra_rds.dto.board.ResponsePostDTO;
import com.harpsharp.infra_rds.dto.todo.ResponseTodoCommentDTO;
import com.harpsharp.infra_rds.dto.todo.ResponseTodoPostDTO;

import java.time.LocalDateTime;
import java.util.Map;

public record ResponseUserDTO(
        String username,
        String email,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime createdAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime updatedAt,
        String socialType,
        String role,
        Map<Long, ResponsePostDTO> posts,
        Map<Long, ResponseCommentDTO> comments,
        Map<Long, ResponseTodoPostDTO> todoPosts,
        Map<Long, ResponseTodoCommentDTO> todoComments)
{
}
