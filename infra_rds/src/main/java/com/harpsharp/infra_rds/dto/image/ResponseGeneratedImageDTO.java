package com.harpsharp.infra_rds.dto.image;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.harpsharp.infra_rds.dto.todo.TodoStatus;

import java.time.LocalDateTime;

public record ResponseGeneratedImageDTO(String url,
                                        String username,
                                        String model,
                                        String prompt,
                                        ImageType type,
                                        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
                                        LocalDateTime createdAt,
                                        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
                                        LocalDateTime updatedAt) {
}
