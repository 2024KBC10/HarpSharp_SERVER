package com.harpsharp.infra_rds.dto.user;

import com.harpsharp.infra_rds.dto.board.ResponseCommentDTO;
import com.harpsharp.infra_rds.dto.board.ResponsePostDTO;

import java.time.LocalDateTime;
import java.util.List;

public record ResponseUserDTO(
        String username,
        String email,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String social_type,
        String role,
        List<ResponsePostDTO> posts,
        List<ResponseCommentDTO> comments)
{
}
