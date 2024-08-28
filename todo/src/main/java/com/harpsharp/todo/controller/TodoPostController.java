package com.harpsharp.todo.controller;

import com.harpsharp.infra_rds.dto.response.ApiResponse;
import com.harpsharp.infra_rds.dto.response.ResponseWithData;
import com.harpsharp.infra_rds.dto.todo.RequestTodoPostDTO;
import com.harpsharp.infra_rds.dto.todo.RequestTodoPostUpdateStatusDTO;
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

    @GetMapping("/api/v1/todo/posts")
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

    @GetMapping("/api/v1/todo/posts/{postId}")
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

    @PostMapping("/api/v1/todo/posts")
    public ResponseEntity<ResponseWithData<Map<Long, ResponseTodoPostDTO>>> createPost(
            @RequestBody RequestTodoPostDTO postDTO) {

        Map<Long, ResponseTodoPostDTO> todoPost = todoPostService.saveTodoPost(postDTO);
        ResponseWithData<Map<Long, ResponseTodoPostDTO>> apiResponse =
                new ResponseWithData<>(
                        LocalDateTime.now(),
                        HttpStatus.CREATED.value(),
                        "GET_POST_BY_POSTID",
                        "TODO 게시글이 정상적으로 작성 되었습니다.",
                        todoPost);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }

    @PatchMapping("/api/v1/todo/posts")
    public ResponseEntity<ResponseWithData<Map<Long, ResponseTodoPostDTO>>> updatePost(
            @RequestBody RequestUpdateTodoPostDTO postDTO) {

        Map<Long, ResponseTodoPostDTO> todoPost = todoPostService.updateTodoPost(postDTO);
        ResponseWithData<Map<Long, ResponseTodoPostDTO>> apiResponse =
                new ResponseWithData<>(
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        "UPDATE_TODO_SUCCESSFULLY",
                        "TODO 게시글이 정상적으로 업데이트 되었습니다.",
                        todoPost
                );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }

    @PatchMapping("/api/v1/todo/posts/status")
    public ResponseEntity<ResponseWithData<Map<Long, ResponseTodoPostDTO>>> updatePost(
            @RequestBody RequestTodoPostUpdateStatusDTO postDTO) {

        Map<Long, ResponseTodoPostDTO> todoPost = todoPostService.updateTodoStatus(postDTO);
        ResponseWithData<Map<Long, ResponseTodoPostDTO>> apiResponse =
                new ResponseWithData<>(
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        "UPDATE_TODO_STATUS_SUCCESSFULLY",
                        "TODO 게시글의 진행 상태가 정상적으로 업데이트 되었습니다.",
                        todoPost
                );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }

    @DeleteMapping("/api/v1/todo/posts")
    public ResponseEntity<ApiResponse> deletePost(
            @RequestBody RequestUpdateTodoPostDTO requestTodoPostDTO) {

        Long postId = requestTodoPostDTO.postId();

        todoPostService.deleteTodoPost(postId);
        ApiResponse apiResponse = new ApiResponse(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "DELETE_TODO_SUCCESSFULLY",
                "게시글이 성공적으로 삭제되었습니다."
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }
}
