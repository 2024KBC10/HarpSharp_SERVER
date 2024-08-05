package com.harpsharp.board.controller;

import com.harpsharp.auth.utils.BaseResponse;
import com.harpsharp.infra_rds.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/board")
    public ResponseEntity<ApiResponse> home() {
        return BaseResponse.withCode("WELCOME_TO_BOARD", "This is Homepage Of Board!", HttpStatus.OK);
    }
}
