package com.harpsharp.todo.controller;

import com.harpsharp.infra_rds.dto.response.ApiResponse;
import com.harpsharp.infra_rds.dto.response.ResponseWithData;
import com.harpsharp.infra_rds.dto.todo.RequestTodoCommentDTO;
import com.harpsharp.infra_rds.dto.todo.RequestUpdateTodoCommentDTO;
import com.harpsharp.infra_rds.dto.todo.ResponseTodoCommentDTO;
import com.harpsharp.todo.service.TodoCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class TodoCommentController {

    private final TodoCommentService todoCommentService;

    @GetMapping("/api/v1/todo/comments")
    public ResponseEntity<ResponseWithData<Map<Long, ResponseTodoCommentDTO>>> getAllComments() {
        Map<Long, ResponseTodoCommentDTO> allComments = todoCommentService.getAllComments();
        ResponseWithData<Map<Long, ResponseTodoCommentDTO>> apiResponse =
                new ResponseWithData<>(
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        "GET_ALL_TODO_COMMENTS_SUCCESSFULLY",
                        "모든 TODO 댓글이 정상적으로 조회되었습니다.",
                        allComments);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }

    @GetMapping("/api/v1/todo/posts/{postId}/comments")
    public ResponseEntity<ResponseWithData<Map<Long,ResponseTodoCommentDTO>>> getAllPostComments(@PathVariable Long postId) {
        Map<Long, ResponseTodoCommentDTO> comment = todoCommentService.getCommentsByPostId(postId);

        ResponseWithData<Map<Long,ResponseTodoCommentDTO>> apiResponse =
                new ResponseWithData<>(
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        "FETCHED_COMMENTS_IN_POST",
                        postId + " 게시글에 달린 댓글들이 정상적으로 조회되었습니다.",
                        comment);


        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }
    @GetMapping("/api/v1/todo/posts/{postId}/comments/{commentId}")
    public ResponseEntity<ResponseWithData<Map<Long, ResponseTodoCommentDTO>>> getCommentById(@PathVariable Long commentId) {
        Map<Long, ResponseTodoCommentDTO> object
                = todoCommentService.getCommentById(commentId);

        ResponseWithData<Map<Long, ResponseTodoCommentDTO>> apiResponse
                = new ResponseWithData<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "UPDATE_TODO_COMMNET_SUCCESSFULLY",
                "댓글이 성공적으로 조회 되었습니다.",
                object);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }

    @PostMapping("/api/v1/todo/posts/comments")
    public ResponseEntity<ResponseWithData<Map<Long, ResponseTodoCommentDTO>>> addComment(
            @RequestBody RequestTodoCommentDTO commentDTO) {

        Map<Long, ResponseTodoCommentDTO> object =
                todoCommentService.addComment(commentDTO);

        ResponseWithData<Map<Long, ResponseTodoCommentDTO>> apiResponse
                = new ResponseWithData<>(
                LocalDateTime.now(),
                HttpStatus.CREATED.value(),
                "UPDATE_TODO_COMMNET_SUCCESSFULLY",
                "댓글이 성공적으로 작성 되었습니다.",
                object);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }

    @PatchMapping("/api/v1/todo/posts/comments")
    public ResponseEntity<ResponseWithData<Map<Long, ResponseTodoCommentDTO>>> updateComment(
            @RequestBody RequestUpdateTodoCommentDTO commentDTO) throws IllegalAccessException {

        Map<Long, ResponseTodoCommentDTO> object =
                todoCommentService.updateComment(commentDTO);

        ResponseWithData<Map<Long, ResponseTodoCommentDTO>> apiResponse
                = new ResponseWithData<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "UPDATE_TODO_COMMNET_SUCCESSFULLY",
                "댓글이 성공적으로 수정 되었습니다.",
                object);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }

    @DeleteMapping("/api/v1/todo/posts/comments")
    public ResponseEntity<ApiResponse> deleteComment(
            @RequestBody RequestUpdateTodoCommentDTO commentDTO) throws IllegalAccessException {

        Long commentId = commentDTO.commentId();

        todoCommentService.deleteComment(commentId);

        ApiResponse apiResponse = new ApiResponse(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "DELETE_COMMNET_SUCCESSFULLY",
                "댓글이 성공적으로 삭제 되었습니다."
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }
}
