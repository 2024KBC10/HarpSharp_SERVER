package com.harpsharp.auth.controller;

import com.harpsharp.infra_rds.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class LoginController {

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> test(){
        try{
            ApiResponse apiResponse = new ApiResponse(
                    LocalDateTime.now(),
                    HttpStatus.OK.value(),
                    "VALID_USER",
                    "로그인에 성공하였습니다."
                    );
            return ResponseEntity
                    .status(HttpStatus.OK.value())
                    .body(apiResponse);
        }
        catch(Exception e){
            ApiResponse apiResponse = new ApiResponse(
                    LocalDateTime.now(),
                    HttpStatus.UNAUTHORIZED.value(),
                    "INVALID_USER",
                    "로그인에 실패하였습니다."
            );
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .body(apiResponse);
        }
    }

}
