package com.harpsharp.auth.controller;

import com.harpsharp.infra_rds.dto.user.JoinDTO;
import com.harpsharp.infra_rds.dto.response.ApiResponse;

import com.harpsharp.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;

@Controller
@ResponseBody
@RequiredArgsConstructor
public class JoinController {
    private final UserService userService;

    @PostMapping("/api/v1/join")
    public ResponseEntity<ApiResponse> joinUser(@RequestBody JoinDTO joinDTO){
        userService.registerUser(joinDTO, "ROLE_USER");

        ApiResponse apiResponse =
                new ApiResponse(LocalDateTime.now(),
                        HttpStatus.CREATED.value(),
                        "USER_JOINED_SUCCESSFULLY",
                joinDTO.username() + "님이 성공적으로 가입 되셨습니다.");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }

    @PostMapping("/api/v1/admin/join")
    public ResponseEntity<ApiResponse> joinAdmin(@RequestBody JoinDTO joinDTO){
        userService.registerUser(joinDTO, "ROLE_ADMIN");
        ApiResponse apiResponse =
                new ApiResponse(
                        LocalDateTime.now(),
                        HttpStatus.CREATED.value(),
                        "USER_JOINED_SUCCESSFULLY",
                        joinDTO.username() + "님이 성공적으로 가입 되셨습니다.");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }
}
