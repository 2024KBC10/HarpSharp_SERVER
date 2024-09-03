package com.harpsharp.infra_rds.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.harpsharp.infra_rds.dto.board.comment.ResponseCommentDTO;
import com.harpsharp.infra_rds.dto.board.post.ResponsePostDTO;
import com.harpsharp.infra_rds.dto.todo.comment.ResponseTodoCommentDTO;
import com.harpsharp.infra_rds.dto.todo.post.ResponseTodoPostDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record ResponseUserDTO(
        String username,
        String email,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime createdAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime updatedAt,
        Position position,
        String socialType,
        String role,
        Map<Long, ResponsePostDTO> posts,
        Map<Long, ResponseCommentDTO> comments,
        Map<Long, ResponseTodoPostDTO> todoPosts,
        Map<Long, ResponseTodoCommentDTO> todoComments,
        List<Long> likeComments)
{
}
