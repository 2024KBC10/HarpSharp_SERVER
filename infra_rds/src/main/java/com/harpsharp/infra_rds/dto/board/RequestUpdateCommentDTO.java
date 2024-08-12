package com.harpsharp.infra_rds.dto.board;

public record RequestUpdateCommentDTO(
        Long commentId,
        String username,
        String content,
        String memoColor,
        String pinColor)
{
}
