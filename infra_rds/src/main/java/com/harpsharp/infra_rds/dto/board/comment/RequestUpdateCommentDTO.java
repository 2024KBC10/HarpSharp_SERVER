package com.harpsharp.infra_rds.dto.board.comment;

public record RequestUpdateCommentDTO(
        Long commentId,
        String username,
        String content,
        String memoColor,
        String pinColor)
{
}
