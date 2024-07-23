package com.harpsharp.auth.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.harpsharp.auth.dto.RedisDTO;
import com.harpsharp.auth.dto.UserDTO;
import com.harpsharp.auth.jwt.JwtUtil;
import com.harpsharp.auth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@Tag(name = "회원 정보 수정", description = "")
public class UserController {
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Operation(summary = "회원 정보 수정")
    @ApiResponse(responseCode = "200", description = "", content = @Content(schema = @Schema(implementation = UserDTO.class)))
    @PatchMapping("/user/update")
    public ResponseEntity<UserDTO> updateUser(@RequestHeader("access")String accessToken,
                                            @RequestBody UserDTO updatedDTO,
                                            HttpServletResponse response) throws IOException {
        // 수정 로직
        Long userId = jwtUtil.getUserId(accessToken);
        System.out.println("user_id = " + userId);
        String updatedUsername = jwtUtil.getUsername(updatedDTO.getUsername());
        String role = jwtUtil.getRole(accessToken);

        userService.updateUser(userId, updatedDTO);

        String newAccess  = jwtUtil.createAccessToken (userId, updatedUsername, role);
        String newRefresh = jwtUtil.createRefreshToken(userId, updatedUsername, role);

        // DB에 존재하는 Refresh 토큰 삭제 후 재발급
        jwtUtil.deleteByAccess(accessToken);
        jwtUtil.addRefreshEntity(newAccess, newRefresh);

        response.setHeader("access", newAccess);
        response.addCookie(jwtUtil.createCookie("refresh", newRefresh));

        return ResponseEntity.status(HttpStatus.OK).body(updatedDTO);
    }

}
