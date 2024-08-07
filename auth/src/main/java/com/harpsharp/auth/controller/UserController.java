package com.harpsharp.auth.controller;


import com.harpsharp.infra_rds.dto.user.ResponseUserDTO;
import com.harpsharp.infra_rds.dto.user.UpdateUserDTO;
import com.harpsharp.infra_rds.dto.response.ApiResponse;
import com.harpsharp.auth.jwt.JwtUtil;
import com.harpsharp.auth.service.UserService;
import com.harpsharp.auth.utils.BaseResponse;
import com.harpsharp.infra_rds.entity.Post;
import com.harpsharp.infra_rds.entity.User;
import com.harpsharp.infra_rds.mapper.UserMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @GetMapping("/user/{id}")
    public ResponseEntity<ResponseUserDTO> getPostById(@PathVariable Long id) {
        User user = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + id));

        ResponseUserDTO responseUserDTO =
                userMapper.convertUserToResponse(user);

        return new ResponseEntity<>(responseUserDTO, HttpStatus.OK);
    }
    @PatchMapping("/user")
    public ResponseEntity<ApiResponse> updateUser(HttpServletRequest request,
                                                  @RequestBody UpdateUserDTO updatedDTO,
                                                  HttpServletResponse response) throws IOException {
        String accessToken = request.getHeader("Authorization");

        if (accessToken == null || updatedDTO == null || !accessToken.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid access token");
        }

        accessToken = accessToken.substring("Bearer ".length());

        Long userId = jwtUtil.getUserId(accessToken);
        userService.updateUser(userId, updatedDTO);

        Cookie[] cookies = request.getCookies();

        if (cookies == null) throw new IllegalArgumentException("Invalid request");

        for(Cookie cookie: cookies){
            response.addCookie(cookie);
        }

        String host = request.getHeader("Host");
        String scheme = request.getHeader("X-Forwarded-Proto");
        String redirectURI = scheme + "://" + host + "/reissue";

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectURI));
        System.out.println(URI.create(redirectURI));
        headers.add("Authorization", "Bearer " + accessToken);

        return new ResponseEntity<>(headers, HttpStatus.TEMPORARY_REDIRECT);
    }

    @DeleteMapping("/user")
    public ResponseEntity<ApiResponse> deleteUser(@RequestHeader("Authorization") String accessToken) throws IOException {

        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            System.out.println("invalid access");
            throw new IllegalArgumentException("Invalid access");
        }

        accessToken = accessToken.substring("Bearer ".length());

        Long userId = jwtUtil.getUserId(accessToken);
        String username = jwtUtil.getUsername(accessToken);
        userService.deleteById(userId, accessToken);

        ApiResponse apiResponse = new ApiResponse(
                "DELETED_SUCCESSFULLY",
                username + " successfully deleted.");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }

}