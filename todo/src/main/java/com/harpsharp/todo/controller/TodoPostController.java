package com.harpsharp.todo.controller;

import com.harpsharp.auth.jwt.JwtUtil;
import com.harpsharp.infra_rds.dto.response.ApiResponse;
import com.harpsharp.infra_rds.dto.response.ResponseWithData;
import com.harpsharp.infra_rds.dto.todo.RequestTodoPostDTO;
import com.harpsharp.infra_rds.dto.todo.RequestUpdateTodoPostDTO;
import com.harpsharp.infra_rds.dto.todo.ResponseTodoPostDTO;
import com.harpsharp.todo.service.TodoPostService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class TodoPostController {

    private final TodoPostService todoPostService;
    private final JwtUtil jwtUtil;

    @GetMapping("/todo/posts")
    public ResponseEntity<ResponseWithData<Map<Long, ResponseTodoPostDTO>>> getAllPosts() {
        Map<Long, ResponseTodoPostDTO> allPosts = todoPostService.getAllTodoPosts();
        ResponseWithData<Map<Long, ResponseTodoPostDTO>> apiResponse =
                new ResponseWithData<>(
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        "GET_ALL_TODO_SUCEESFULLY",
                        "모든 TODO 게시글이 정상적으로 조회되었습니다.",
                        allPosts);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }

    @GetMapping("/todo/posts/{postId}")
    public ResponseEntity<ResponseWithData<Map<Long, ResponseTodoPostDTO>>> getPostById(@PathVariable Long postId) {
        Map<Long, ResponseTodoPostDTO> todoPost = todoPostService.getTodoPostById(postId);
        ResponseWithData<Map<Long, ResponseTodoPostDTO>> apiResponse =
                new ResponseWithData<>(
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        "GET_POST_BY_POSTID",
                        "해당 TODO 게시글이 정상적으로 조회되었습니다.",
                        todoPost);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }

    @PostMapping("/todo/posts")
    public ResponseEntity<ResponseWithData<Map<Long, ResponseTodoPostDTO>>> createPost(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody RequestTodoPostDTO postDTO) {

        if(!isValid(accessToken, postDTO.username()))
            throw new IllegalArgumentException("INVALID_ACCESS");

        Map<Long, ResponseTodoPostDTO> todoPost = todoPostService.saveTodoPost(postDTO);
        ResponseWithData<Map<Long, ResponseTodoPostDTO>> apiResponse =
                new ResponseWithData<>(
                        LocalDateTime.now(),
                        HttpStatus.CREATED.value(),
                        "GET_POST_BY_POSTID",
                        "TODO 게시글이 정상적으로 작성 되었습니다.",
                        todoPost);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(apiResponse);
    }

    @PatchMapping("/todo/posts")
    public ResponseEntity<ResponseWithData<Map<Long, ResponseTodoPostDTO>>> updatePost(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody RequestUpdateTodoPostDTO postDTO) {

        if(!isValid(accessToken, postDTO.username()))
            throw new IllegalArgumentException("INVALID_ACCESS");


        Map<Long, ResponseTodoPostDTO> todoPost = todoPostService.updateTodoPost(postDTO);
        ResponseWithData<Map<Long, ResponseTodoPostDTO>> apiResponse =
                new ResponseWithData<>(
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        "UPDATE_POST_SUCCESSFULLY",
                        "TODO 게시글이 정상적으로 업데이트 되었습니다.",
                        todoPost
                );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }

    @DeleteMapping("/todo/posts")
    public ResponseEntity<ApiResponse> deletePost(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody RequestUpdateTodoPostDTO requestTodoPostDTO) {

        if(!isValid(accessToken, requestTodoPostDTO.username()))
            throw new IllegalArgumentException("INVALID_ACCESS");

        Long postId = requestTodoPostDTO.postId();

        todoPostService.deleteTodoPost(postId);
        ApiResponse apiResponse = new ApiResponse(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "REDIRECT_TO_TODO",
                "게시글이 성공적으로 삭제되었습니다."
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }

    @NotNull
    private Boolean isValid(String accessToken, String username) {
        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            return false;
        }

        accessToken = accessToken.substring("Bearer ".length());


        return username.equals(jwtUtil.getUsername(accessToken));
    }
}
