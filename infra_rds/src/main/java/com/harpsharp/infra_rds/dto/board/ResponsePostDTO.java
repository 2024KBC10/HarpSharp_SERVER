package com.harpsharp.board.dto;

import lombok.*;

import java.time.LocalDateTime;

public record ResponsePostDTO(String username,
                              String title,
                              String content,
                              LocalDateTime createdAt) {
}
