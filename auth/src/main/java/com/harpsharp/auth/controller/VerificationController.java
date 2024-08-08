package com.harpsharp.auth.controller;

import com.harpsharp.auth.jwt.JwtUtil;
import com.harpsharp.infra_rds.dto.board.RequestCommentDTO;
import com.harpsharp.infra_rds.dto.board.RequestPostDTO;
import com.harpsharp.infra_rds.dto.board.RequestUpdatePostDTO;
import com.harpsharp.infra_rds.dto.response.ApiResponse;
import jakarta.validation.constraints.NotNull;
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
    public ResponseEntity<ApiResponse> verificationBoard(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody RequestPostDTO postDTO) {
        return isValid(accessToken, postDTO.username());
    }

    @GetMapping("/verify/posts")
    public ResponseEntity<ApiResponse> verificationBoard(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody RequestUpdatePostDTO postDTO) {

        return isValid(accessToken, postDTO.username());
    }

    @NotNull
    private ResponseEntity<ApiResponse> isValid(@RequestHeader("Authorization") String accessToken, String username) {
        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            System.out.println("invalid access");
            throw new IllegalArgumentException("Invalid access");
        }

        accessToken = accessToken.substring("Bearer ".length());

        if(!username.equals(jwtUtil.getUsername(accessToken)))
            throw new IllegalArgumentException("INVALID_USERNAME");

        ApiResponse apiResponse = new ApiResponse(
                "VERIFIED_SUCCESS",
                "회원 권한이 확인 되었습니다.");
        System.out.println("apiResponse = " + apiResponse);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }

    // @GetMapping(/verify/board/posts/{postId}
    // @GetMapping(/verify/board/comments/{commentId}
    // @GetMapping(/verify/todo/posts/{todoPostId}
    // @GetMapping(/verify/todo/comments/{todoComentId}
}
