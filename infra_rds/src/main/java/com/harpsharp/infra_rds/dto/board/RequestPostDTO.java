package com.harpsharp.board.dto;
import java.time.LocalDateTime;


public record RequestPostDTO(String username,
                             String title,
                             String content) {
}
