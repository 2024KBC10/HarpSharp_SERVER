package com.harpsharp.infra_rds.dto.board;

public record RequestUpdatePostDTO(Long postId,
                                   String username,
                                   String title,
                                   String content,
                                   String memoColor,
                                   String pinColor)
{}
