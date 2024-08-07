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

    @GetMapping("/verify")
    public ResponseEntity<?> verificationBoard() {

        ApiResponse apiResponse = new ApiResponse(
                "VERIFIED_SUCCESS",
                "회원 권한이 확인 되었습니다.");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }

    // @GetMapping(/verify/board/posts/{postId}
    // @GetMapping(/verify/board/comments/{commentId}
    // @GetMapping(/verify/todo/posts/{todoPostId}
    // @GetMapping(/verify/todo/comments/{todoComentId}
}
