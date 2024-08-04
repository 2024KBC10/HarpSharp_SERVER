package com.harpsharp.auth.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class ApiResponse {
    private final String code;
    private final String message;
}
