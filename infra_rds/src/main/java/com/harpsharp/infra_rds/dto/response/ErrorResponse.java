package com.harpsharp.infra_rds.dto.response;

import jakarta.servlet.http.HttpServletResponse;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {
    private final String code;
    private final String message;
}