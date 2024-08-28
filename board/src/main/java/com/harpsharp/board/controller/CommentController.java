package com.harpsharp.board.controller;
import com.harpsharp.infra_rds.dto.board.*;
import com.harpsharp.board.service.CommentService;
import com.harpsharp.infra_rds.dto.response.ApiResponse;
import com.harpsharp.infra_rds.dto.response.ResponseWithData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/api/v1/board/posts/{postId}/comments")
    public ResponseEntity<ResponseWithData<Map<Long, ResponseCommentDTO>>> getCommentsByPostId(@PathVariable Long postId) {

        Map<Long, ResponseCommentDTO> comments = commentService.getCommentsByPostId(postId);
        ResponseWithData<Map<Long, ResponseCommentDTO>> apiResponse
                = new ResponseWithData<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "COMMETS_BY_POST_ID",
                "postId: "+postId.toString() + " 게시글에 속한 댓글들입니다.",
                comments);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }

    @GetMapping("/api/v1/board/posts/{postId}/comments/{commentId}")
    public ResponseEntity<ResponseWithData<Map<Long, ResponseCommentDTO>>> getCommentByCommentId(@PathVariable Long commentId) {
        Map<Long, ResponseCommentDTO> comment = commentService.getCommentById(commentId);
        ResponseWithData<Map<Long, ResponseCommentDTO>> apiResponse
                = new ResponseWithData<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "COMMETS_BY_POST_ID",
                "요청한 댓글이 성공적으로 조회되었습니다.",
                comment);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }

    @PostMapping("/api/v1/board/posts/comments")
    public ResponseEntity<ResponseWithData<Map<Long, ResponseCommentDTO>>> addComment(
            @RequestBody RequestCommentDTO commentDTO) throws IllegalAccessException {;

        Map<Long, ResponseCommentDTO> object = commentService.save(commentDTO);

        ResponseWithData<Map<Long, ResponseCommentDTO>> apiResponse
                = new ResponseWithData<>(
                LocalDateTime.now(),
                HttpStatus.CREATED.value(),
                "ADD_COMMNET_SUCCESSFULLY",
                "댓글이 성공적으로 작성되었습니다.",
                object);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);

    }

    @PatchMapping("/api/v1/board/posts/comments")
    public ResponseEntity<ResponseWithData<Map<Long, ResponseCommentDTO>>> updateComment(
            @RequestBody RequestUpdateCommentDTO updatedComment) throws IllegalAccessException {

        Map<Long, ResponseCommentDTO> object
                = commentService.updateComment(updatedComment);


        ResponseWithData<Map<Long, ResponseCommentDTO>> apiResponse
                = new ResponseWithData<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "UPDATE_COMMNET_SUCCESSFULLY",
                "댓글이 성공적으로 수정되었습니다.",
                object);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }


    @DeleteMapping("/api/v1/board/posts/comments")
    public ResponseEntity<ApiResponse> deleteComment(
            @RequestBody RequestUpdateCommentDTO commentDTO) throws IllegalAccessException {

        Long commentId = commentDTO.commentId();

        commentService.deleteComment(commentId);

        ApiResponse apiResponse = new ApiResponse(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "DELETE_COMMNET_SUCCESSFULLY",
                "댓글이 성공적으로 삭제되었습니다.");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }
}
