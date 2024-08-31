package com.harpsharp.auth.controller;

import com.harpsharp.auth.jwt.JwtUtil;
import com.harpsharp.infra_rds.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@Slf4j
public class VerificationController {
    private final JwtUtil jwtUtil;

    @GetMapping("/api/v1/verify")
    public ResponseEntity<ApiResponse> verificationCreatePost() {
        ApiResponse apiResponse = new ApiResponse(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "VERIFIED_SUCCESS",
                "회원 권한이 확인 되었습니다.");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }
}
