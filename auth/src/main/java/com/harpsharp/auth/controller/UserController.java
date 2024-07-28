package com.harpsharp.auth.controller;


import com.harpsharp.auth.dto.response.ApiResponse;
import com.harpsharp.auth.jwt.JwtUtil;
import com.harpsharp.auth.service.UserService;
import com.harpsharp.infra_rds.dto.UserDTO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @PatchMapping("/user/update")
    public ResponseEntity<ApiResponse> updateUser(@RequestHeader("Authorization")String accessToken,
                                                  @RequestBody UserDTO updatedDTO,
                                                  HttpServletResponse response) throws IOException {
        // 수정 로직
        if(accessToken == null || updatedDTO == null || !accessToken.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid access token");
        }

        accessToken = accessToken.substring("Bearer ".length());

        Long userId = jwtUtil.getUserId(accessToken);
        String updatedUsername = updatedDTO.getUsername();
        String role = jwtUtil.getRole(accessToken);

        userService.updateUser(userId, updatedDTO);

        String newAccess  = jwtUtil.createAccessToken (userId, updatedUsername, role);
        String newRefresh = jwtUtil.createRefreshToken(userId, updatedUsername, role);

        // DB에 존재하는 Refresh 토큰 삭제 후 재발급
        jwtUtil.deleteById(userId);
        jwtUtil.addRefreshEntity(userId, newRefresh);

        response.setHeader("Authorization", "Bearer " + newAccess);
        response.addCookie(jwtUtil.createCookie("refresh", newRefresh));

        //return ResponseEntity.status(HttpStatus.OK).body("The user information has been successfully updated.");
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.builder()
                        .code("UPDATE_USER_SUCCESSFULLY")
                        .message("The user information has been successfully updated.")
                        .build());
    }

    @DeleteMapping("/user/delete")
    public ResponseEntity<ApiResponse> updateUser(@RequestHeader("Authorization")String accessToken) throws IOException {
        if(accessToken == null || !accessToken.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid access token");
        }

        Long userId = jwtUtil.getUserId(accessToken.split(" ")[1]);
        userService.deleteById(userId);
        jwtUtil.deleteById(userId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.builder()
                        .code("DELETE_USER_SUCCESSFULLY")
                        .message("The user information has been successfully deleted.")
                        .build());
    }
}
