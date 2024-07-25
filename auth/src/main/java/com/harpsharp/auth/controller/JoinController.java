package com.harpsharp.auth.controller;

import com.harpsharp.auth.dto.JoinDTO;
import com.harpsharp.auth.dto.response.ApiResponse;
import com.harpsharp.auth.service.JoinService;
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.tags.Tag;
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

    private final JoinService joinService;

    @PostMapping("/join")
    public ResponseEntity<ApiResponse> joinUser(@RequestBody JoinDTO joinDTO){
        joinService.registerUser(joinDTO, "ROLE_USER");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.builder()
                        .code("USER_JOINED_SUCCESSFULLY")
                        .message(joinDTO.getUsername() + " has been successfully registered.")
                        .build());
    }

    @PostMapping("/admin/join")
    public ResponseEntity<ApiResponse> joinAdmin(@RequestBody JoinDTO joinDTO){
        joinService.registerUser(joinDTO, "ROLE_ADMIN");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.builder()
                        .code("USER_JOINED_SUCCESSFULLY")
                        .message(joinDTO.getUsername() + " has been successfully registered.")
                        .build());
    }
}
