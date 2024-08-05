package com.harpsharp.auth.controller;

import com.harpsharp.auth.jwt.JwtUtil;
import com.harpsharp.infra_rds.dto.board.RequestCommentDTO;
import com.harpsharp.infra_rds.dto.board.RequestPostDTO;
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

    @RequestMapping(value = "/verify/posts", method = {RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.PATCH})
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

        return ResponseEntity.ok("User verified successfully");
    }


    @RequestMapping(value = "/verify/comments", method = {RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.PATCH})
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

        return ResponseEntity.ok("User verified successfully");
    }
}
