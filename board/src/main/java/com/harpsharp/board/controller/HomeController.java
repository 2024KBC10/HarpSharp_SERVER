package com.harpsharp.board.controller;

import com.harpsharp.infra_rds.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class HomeController {

    @RequestMapping ("/board")
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
}
