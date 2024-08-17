package com.harpsharp.board.controller;

import com.harpsharp.infra_rds.dto.board.testDTO;
import com.harpsharp.infra_rds.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
public class HomeController {

    @GetMapping("/api/v1/board")
    public ResponseEntity<ApiResponse> home() {
        ApiResponse apiResponse = new ApiResponse(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "WELCOME_TO_BOARD",
                "아이스 브레이킹 루트 페이지입니다.");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }

    @PostMapping("/api/v1/board")
    public ResponseEntity<ApiResponse> board(@RequestBody testDTO test) {
        ApiResponse apiResponse = new ApiResponse(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "POST TEST",
                "테스트 성공"
        );


        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);

    }
}
