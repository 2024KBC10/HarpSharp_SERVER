package com.harpsharp.infra_rds.dto.board.post;

public record RequestUpdatePostDTO(Long postId,
                                   String username,
                                   String title,
                                   String content)
{}
