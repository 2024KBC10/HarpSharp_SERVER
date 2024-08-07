package com.harpsharp.auth.controller;

import com.harpsharp.auth.jwt.JwtUtil;
import com.harpsharp.infra_rds.dto.board.RequestCommentDTO;
import com.harpsharp.infra_rds.dto.board.RequestPostDTO;
import com.harpsharp.infra_rds.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class VerificationController {
    private final JwtUtil jwtUtil;

    @GetMapping("/verify/posts")
    public ResponseEntity<?> verificationBoard(@RequestHeader("Authorization") String authorization,
                                                         @RequestBody RequestPostDTO requestBody) {
        // Authorization 헤더에서 JWT 추출
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization header is missing or invalid");
        }

        String accessToken = authorization.substring("Bearer ".length());

        // JWT 클레임과 요청 본문에서 username 추출
        String jwtUsername = jwtUtil.getUsername(accessToken);
        String requestUsername = requestBody.username();

        if (requestUsername == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is missing in request body");
        }

        // username 비교
        if (!jwtUsername.equals(requestUsername)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Username does not match");
        }

        ApiResponse apiResponse = new ApiResponse(
                "VERIFIED_SUCCESS",
                "회원 권한이 확인 되었습니다.");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }


    @GetMapping(value = "/verify/comments")
    public ResponseEntity<?> verificationComment(@RequestHeader("Authorization") String authorization,
                                                @RequestBody RequestCommentDTO requestBody) {
        // Authorization 헤더에서 JWT 추출
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization header is missing or invalid");
        }

        String accessToken = authorization.substring("Bearer ".length());

        // JWT 클레임과 요청 본문에서 username 추출
        String jwtUsername = jwtUtil.getUsername(accessToken);
        String requestUsername = requestBody.username();

        if (requestUsername == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is missing in request body");
        }

        // username 비교
        if (!jwtUsername.equals(requestUsername)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Username does not match");
        }

        ApiResponse apiResponse = new ApiResponse(
                "VERIFIED_SUCCESS",
                "회원 권한이 확인 되었습니다.");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }
}
