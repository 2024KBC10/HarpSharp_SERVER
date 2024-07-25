package com.harpsharp.auth.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.harpsharp.auth.dto.RedisDTO;
import com.harpsharp.auth.dto.UserDTO;
import com.harpsharp.auth.dto.response.ApiResponse;
import com.harpsharp.auth.jwt.JwtUtil;
import com.harpsharp.auth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @PatchMapping("/user/update")
    public ResponseEntity<ApiResponse> updateUser(@RequestHeader("access")String accessToken,
                                                  @RequestBody UserDTO updatedDTO,
                                                  HttpServletResponse response) throws IOException {
        // 수정 로직
        Long userId = jwtUtil.getUserId(accessToken);
        String updatedUsername = updatedDTO.getUsername();
        String role = jwtUtil.getRole(accessToken);

        userService.updateUser(userId, updatedDTO);

        String newAccess  = jwtUtil.createAccessToken (userId, updatedUsername, role);
        String newRefresh = jwtUtil.createRefreshToken(userId, updatedUsername, role);

        // DB에 존재하는 Refresh 토큰 삭제 후 재발급
        jwtUtil.deleteByAccess(accessToken);
        jwtUtil.addRefreshEntity(newAccess, newRefresh);

        response.setHeader("access", newAccess);
        response.addCookie(jwtUtil.createCookie("refresh", newRefresh));

        //return ResponseEntity.status(HttpStatus.OK).body("The user information has been successfully updated.");
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.builder()
                        .code("UPDATE_USER_SUCCESSFULLY")
                        .message("The user information has been successfully updated.")
                        .build());
    }

    @DeleteMapping("/user/delete")
    public ResponseEntity<ApiResponse> updateUser(@RequestHeader("access")String accessToken) throws IOException {
        Long userId = jwtUtil.getUserId(accessToken);
        userService.deleteUser(userId);
        jwtUtil.deleteByAccess(accessToken);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.builder()
                        .code("DELETE_USER_SUCCESSFULLY")
                        .message("The user information has been successfully deleted.")
                        .build());
    }
}
