package com.harpsharp.auth.controller;


import com.harpsharp.infra_rds.dto.board.ResponseCommentDTO;
import com.harpsharp.infra_rds.dto.board.ResponsePostDTO;
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
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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


    @GetMapping("/user")
    public ResponseEntity<ResponseWithData<Map<Long, ResponseUserDTO>>> getUserDataById
            (@RequestBody InfoDTO info) {

        String username = info.username();
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

    @PatchMapping("/user")
    public ResponseEntity<ApiResponse> updateUser(
            HttpServletRequest request,
            @RequestBody UpdateUserDTO updatedDTO) throws IOException {

        String accessToken = request.getHeader("Authorization");

        if (accessToken == null || updatedDTO == null || !accessToken.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid access token");
        }

        accessToken = accessToken.substring("Bearer ".length());

        String username = jwtUtil.getUsername(accessToken);
        Long userId = jwtUtil.getUserId(accessToken);

        userService.updateUser(userId, updatedDTO);

        String redirectURI = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .replacePath("/reissue")
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectURI));
        headers.add("Authorization", "Bearer " + accessToken);
        System.out.println("headers = " + headers);

        ApiResponse apiResponse =
                new ApiResponse(
                        LocalDateTime.now(),
                        HttpStatus.SEE_OTHER.value(),
                        "GO_TO_REISSUE",
                        "유저 정보를 수정하였습니다. 토큰을 재발급 합니다."
                );

        return ResponseEntity
                .status(HttpStatus.SEE_OTHER)
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
        System.out.println("DTO password: " + deleteDTO.password());
        System.out.println("Entity password: " + userService.findPasswordByUsername(username));

        if(!passwordEncoder.matches(deleteDTO.password(), userService.findPasswordByUsername(username)))
            throw new IllegalArgumentException("INVALID_PASSWORD");

        userService.deleteById(userId, accessToken);

        ApiResponse apiResponse = new ApiResponse(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "DELETED_SUCCESSFULLY",
                username + "님이 탈퇴하셨습니다.");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }

    @GetMapping("/user/board/posts")
    public ResponseWithData<Map<Long, ResponsePostDTO>> getPostsByUserInfo(@RequestBody InfoDTO info){
        ResponseUserDTO user = userService
                .findByUsername(info.username())
                .values()
                .stream()
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("INVALID_INFO"));

        Map<Long, ResponsePostDTO> data = user.posts();

        ResponseWithData<Map<Long, ResponsePostDTO>> responseWithData
                = new ResponseWithData<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "SELECT_POSTS_BY_INFO_SUCCESSFULLY",
                user.username() + "님이 작성한 게시글 입니다",
                data);

        return responseWithData;
    }

    @GetMapping("/user/board/comments")
    public ResponseWithData<Map<Long, ResponseCommentDTO>> getCommentsByUserInfo(@RequestBody InfoDTO info){
        ResponseUserDTO user = userService
                        .findByUsername(info.username())
                        .values()
                        .stream()
                        .findAny()
                        .orElseThrow(() -> new IllegalArgumentException("INVALID_INFO"));


        Map<Long, ResponseCommentDTO> data = user.comments();

        ResponseWithData<Map<Long, ResponseCommentDTO>> responseWithData
                = new ResponseWithData<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "SELECT_POSTS_BY_INFO_SUCCESSFULLY",
                user.username() + "님이 작성한 댓글 입니다",
                data);

        return responseWithData;
    }

    @GetMapping("/user/todo/posts")
    public ResponseWithData<Map<Long, ResponseTodoPostDTO>> getTodoPostsByUserInfo(@RequestBody InfoDTO info){
        ResponseUserDTO user = userService
                .findByUsername(info.username())
                .values()
                .stream()
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("INVALID_INFO"));

        Map<Long, ResponseTodoPostDTO> data = user.todoPosts();

        ResponseWithData<Map<Long, ResponseTodoPostDTO>> responseWithData
                = new ResponseWithData<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "SELECT_POSTS_BY_INFO_SUCCESSFULLY",
                user.username() + "님이 작성한 Todo 게시글 입니다",
                data);

        return responseWithData;
    }

    @GetMapping("/user/todo/comments")
    public ResponseWithData<Map<Long, ResponseTodoCommentDTO>> getTodoCommentsByUserInfo(@RequestBody InfoDTO info){
        ResponseUserDTO user = userService
                .findByUsername(info.username())
                .values()
                .stream()
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("INVALID_INFO"));


        Map<Long, ResponseTodoCommentDTO> data = user.todoComments();

        ResponseWithData<Map<Long, ResponseTodoCommentDTO>> responseWithData
                = new ResponseWithData<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "SELECT_POSTS_BY_INFO_SUCCESSFULLY",
                user.username() + "님이 작성한 Todo 댓글 입니다",
                data);

        return responseWithData;
    }

}