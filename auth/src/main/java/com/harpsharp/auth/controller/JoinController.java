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

@Controller
@ResponseBody
@RequiredArgsConstructor
public class JoinController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<ApiResponse> joinUser(@RequestBody JoinDTO joinDTO){
        userService.registerUser(joinDTO, "ROLE_USER");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.builder()
                        .code("USER_JOINED_SUCCESSFULLY")
                        .message(joinDTO.getUsername() + " has been successfully registered.")
                        .build());
    }

    @PostMapping("/admin/join")
    public ResponseEntity<ApiResponse> joinAdmin(@RequestBody JoinDTO joinDTO){
        userService.registerUser(joinDTO, "ROLE_ADMIN");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.builder()
                        .code("USER_JOINED_SUCCESSFULLY")
                        .message(joinDTO.getUsername() + " has been successfully registered.")
                        .build());
    }
}
