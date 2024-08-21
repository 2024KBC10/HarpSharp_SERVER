package com.harpsharp.infra_rds.dto.image;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ResponsePostImageDTO(String url,
                                   String username,
                                   Long postId,
                                   ImageType type,
                                   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
                                   LocalDateTime createdAt,
                                   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
                                   LocalDateTime updatedAt) {
}
