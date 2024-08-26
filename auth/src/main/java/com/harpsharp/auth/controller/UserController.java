package com.harpsharp.auth.controller;


import com.harpsharp.auth.entity.RefreshToken;
import com.harpsharp.auth.service.RefreshTokenService;
import com.harpsharp.infra_rds.dto.board.ResponseCommentDTO;
import com.harpsharp.infra_rds.dto.board.ResponsePostDTO;
import com.harpsharp.infra_rds.dto.image.ResponseProfileImageURLDTO;
import com.harpsharp.infra_rds.dto.response.ResponseWithData;
import com.harpsharp.infra_rds.dto.todo.ResponseTodoCommentDTO;
import com.harpsharp.infra_rds.dto.todo.ResponseTodoPostDTO;
import com.harpsharp.infra_rds.dto.user.DeleteDTO;
import com.harpsharp.infra_rds.dto.user.InfoDTO;
import com.harpsharp.infra_rds.dto.user.ResponseUserDTO;
import com.harpsharp.infra_rds.dto.user.UpdateUserDTO;
import com.harpsharp.infra_rds.dto.response.ApiResponse;
import com.harpsharp.auth.jwt.JwtUtil;
import com.harpsharp.auth.service.UserService;
import com.harpsharp.infra_rds.mapper.UserMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;


    @GetMapping("/api/v1/user/{username}")
    public ResponseEntity<ResponseWithData<Map<Long, ResponseUserDTO>>> getUserDataById(@PathVariable String username) {
        Map<Long, ResponseUserDTO> user = userService.findByUsername(username);


        ResponseWithData<Map<Long, ResponseUserDTO>> responseWithData = new ResponseWithData<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "GET_USER_BY_ID_SUCCESS",
                "요청한 유저 정보를 성공적으로 읽어왔습니다.",
                user
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseWithData);
    }

    @PatchMapping("/api/v1/user")
    public ResponseEntity<ResponseWithData<Map<Long, ResponseUserDTO>>> updateUser(
            HttpServletRequest request,
            @RequestBody UpdateUserDTO updatedDTO) throws Exception {

        String accessToken = request.getHeader("Authorization");

        if (accessToken == null || updatedDTO == null || !accessToken.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid access token");
        }

        accessToken = accessToken.substring("Bearer ".length());

        String username = jwtUtil.getUsername(accessToken);

        Map<Long, ResponseUserDTO> object = userService.updateUser(username, updatedDTO);

        ResponseUserDTO user =
                object.values().stream().findFirst().orElseThrow(()-> new RuntimeException("USER_NOT_FOUND"));


        String newAccess  = jwtUtil.createAccessToken (user.username(), user.role());
        String newRefresh = jwtUtil.createRefreshToken(user.username(), user.role());

        refreshTokenService.deleteByToken(accessToken);

        RefreshToken refreshEntity = RefreshToken
                .builder()
                .accessToken(newAccess)
                .refreshToken(newRefresh)
                .build();

        refreshTokenService.save(refreshEntity);


        HttpHeaders headers = new HttpHeaders();

        headers.set("Authorization", "Bearer " + newAccess);

        Cookie refreshCookie = jwtUtil.createCookie("refresh", newRefresh);
        String cookieHeader = String.format("%s=%s; Path=%s; HttpOnly; Max-Age=%d; SameSite=None; Secure ",
                refreshCookie.getName(),
                refreshCookie.getValue(),
                refreshCookie.getPath(),
                refreshCookie.getMaxAge());

        headers.add(HttpHeaders.SET_COOKIE, cookieHeader);


        ResponseWithData<Map<Long, ResponseUserDTO>> apiResponse =
                new ResponseWithData<Map<Long,ResponseUserDTO>>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "USER_UPDATED_SUCCESSFULLY",
                "유저 정보가 성공적으로 수정되었습니다.",
                        object);

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .body(apiResponse);
    }

    @DeleteMapping("/api/v1/user")
    public ResponseEntity<ApiResponse> deleteUser(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody DeleteDTO deleteDTO) throws IOException {

        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            throw new IllegalArgumentException("INVALID_ACCESS_TOKEN");
        }

        accessToken = accessToken.substring("Bearer ".length());

        String username = jwtUtil.getUsername(accessToken);

        if(!passwordEncoder.matches(deleteDTO.password(), userService.findPasswordByUsername(username)))
            throw new IllegalArgumentException("INVALID_PASSWORD");

        userService.deleteByUsername(username, accessToken);
        jwtUtil.deleteByToken(accessToken);

        ApiResponse apiResponse = new ApiResponse(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "DELETED_SUCCESSFULLY",
                username + "님이 탈퇴하셨습니다.");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }

    @GetMapping("/api/v1/user/board/posts/{username}")
    public ResponseWithData<Map<Long, ResponsePostDTO>> getPostsByUserInfo(@PathVariable String username){
        Map<Long, ResponsePostDTO> data = userService.findPostsByUsername(username);

        return new ResponseWithData<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "SELECT_POSTS_BY_INFO_SUCCESSFULLY",
                username + "님이 작성한 게시글입니다",
                data);
    }

    @GetMapping("/api/v1/user/board/comments/{username}")
    public ResponseWithData<Map<Long, ResponseCommentDTO>> getCommentsByUserInfo(@PathVariable String username){
        Map<Long, ResponseCommentDTO> data = userService.findCommentsByUsername(username);

        return new ResponseWithData<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "SELECT_POSTS_BY_INFO_SUCCESSFULLY",
                username + "님이 작성한 댓글입니다",
                data);
    }

    @GetMapping("/api/v1/user/todo/posts/{username}")
    public ResponseWithData<Map<Long, ResponseTodoPostDTO>> getTodoPostsByUserInfo(@PathVariable String username){
        ResponseUserDTO user = userService
                .findByUsername(username)
                .values()
                .stream()
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

        Map<Long, ResponseTodoPostDTO> data = userService.findTodoPostsByUsername(username);


        return new ResponseWithData<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "SELECT_POSTS_BY_INFO_SUCCESSFULLY",
                username + "님이 작성한 Todo 게시글입니다",
                data);
    }

    @GetMapping("/api/v1/user/todo/comments/{username}")
    public ResponseWithData<Map<Long, ResponseTodoCommentDTO>> getTodoCommentsByUserInfo(@PathVariable String username){
        Map<Long, ResponseTodoCommentDTO> data = userService.findTodoCommentsByUsername(username);

        return new ResponseWithData<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "SELECT_POSTS_BY_INFO_SUCCESSFULLY",
                username + "님이 작성한 Todo 댓글입니다",
                data);
    }

    @GetMapping("/api/v1/user/profile/{username}")
    public ResponseWithData<ResponseProfileImageURLDTO> getUserProfileImage(@PathVariable String username){
        ResponseProfileImageURLDTO data = userService.findProfileUrlByUsername(username);

        return new ResponseWithData<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "SELECT_URL_BY_USERNAME_SUCCESSFULLY",
                username + "님의 프로필 이미지 URL입니다.",
                data
        );
    }

}