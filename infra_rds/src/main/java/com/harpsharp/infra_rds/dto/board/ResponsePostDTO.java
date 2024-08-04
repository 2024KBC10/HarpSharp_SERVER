package com.harpsharp.infra_rds.dto.board;

import java.time.LocalDateTime;
import java.util.List;

public record ResponsePostDTO(String username,
                              String title,
                              String content,
                              LocalDateTime createdAt,
                              LocalDateTime updatedAt,
                              List<ResponseCommentDTO> comments) {
}
