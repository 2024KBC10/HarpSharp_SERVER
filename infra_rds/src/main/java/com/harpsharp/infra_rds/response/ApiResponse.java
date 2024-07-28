package com.harpsharp.infra_rds.response;

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
