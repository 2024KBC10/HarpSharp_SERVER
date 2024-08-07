package com.harpsharp.infra_rds.dto.board;

public record RequestUpdateCommnetDTO(Long commentId,
                                      String username,
                                      String content)
{
}
