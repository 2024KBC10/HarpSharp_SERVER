package com.harpsharp.auth.controller;


import com.harpsharp.infra_rds.dto.response.ResponseWithData;
import com.harpsharp.infra_rds.dto.user.DeleteDTO;
import com.harpsharp.infra_rds.dto.user.ResponseUserDTO;
import com.harpsharp.infra_rds.dto.user.UpdateUserDTO;
import com.harpsharp.infra_rds.dto.response.ApiResponse;
import com.harpsharp.auth.jwt.JwtUtil;
import com.harpsharp.auth.service.UserService;
import com.harpsharp.infra_rds.dto.user.UserDTO;
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
    public ResponseEntity<ResponseWithData<ResponseUserDTO>> getPostById(@PathVariable Long id) {
        User user = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + id));

        ResponseUserDTO responseUserDTO =
                userMapper.convertUserToResponse(user);

        ResponseWithData<ResponseUserDTO> responseWithData = new ResponseWithData<>(
                "GET_USER_BY_ID_SUCCESS",
                "요청한 유저 정보를 성공적으롤 읽어왔습니다.",
                responseUserDTO
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseWithData);
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
        String redirectURI = "http://" + host + "/reissue";

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectURI));
        System.out.println(URI.create(redirectURI));
        headers.add("Authorization", "Bearer " + accessToken);
        System.out.println("Go To Redirect: " + redirectURI);
        ApiResponse apiResponse = new ApiResponse(
                "GO_TO_REISSUE",
                "유저 정보를 수정하였습니다. 토큰을 재발급 합니다.");

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .headers(headers)
                .body(apiResponse);
    }

    @DeleteMapping("/user")
    public ResponseEntity<ApiResponse> deleteUser(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody DeleteDTO deleteDTO) throws IOException {

        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            System.out.println("invalid access");
            throw new IllegalArgumentException("Invalid access");
        }

        accessToken = accessToken.substring("Bearer ".length());

        Long userId = jwtUtil.getUserId(accessToken);
        String username = jwtUtil.getUsername(accessToken);

        if(!passwordEncoder.matches(deleteDTO.password(), userService.findPasswordByUsername(username)))
            throw new IllegalArgumentException("INVALID_PASSWORD");

        userService.deleteById(userId, accessToken);

        ApiResponse apiResponse = new ApiResponse(
                "DELETED_SUCCESSFULLY",
                username + " successfully deleted.");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }

}