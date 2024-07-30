package com.harpsharp.auth.utils;

import com.harpsharp.auth.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BaseResponse {
    public static ResponseEntity<ApiResponse> withCode(String code, String msg, HttpStatus status){
        return ResponseEntity.status(status)
                .body(ApiResponse.builder()
                        .code(code)
                        .message(msg)
                        .build());
    }
}
