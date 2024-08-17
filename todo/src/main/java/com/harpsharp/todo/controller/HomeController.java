package com.harpsharp.todo.controller;

import com.harpsharp.infra_rds.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class HomeController {
    @GetMapping("/api/v1/todo")
    public ResponseEntity<ApiResponse> home() {
        ApiResponse apiResponse = new ApiResponse(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "WELCOME_TO_TODO",
                "TODO 루트 페이지입니다.");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }
}
