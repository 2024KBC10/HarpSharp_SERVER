package com.harpsharp.board.controller;

import com.harpsharp.infra_rds.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/board")
    public ResponseEntity<ApiResponse> home() {
        ApiResponse apiResponse = new ApiResponse(
                "WELCOME_TO_BOARD",
                "This is Homepage Of Board!");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }
}
