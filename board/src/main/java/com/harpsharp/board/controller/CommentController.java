package com.harpsharp.board.controller;
import com.harpsharp.auth.jwt.JwtUtil;
import com.harpsharp.infra_rds.dto.board.*;
import com.harpsharp.board.service.CommentService;
import com.harpsharp.infra_rds.dto.response.ApiResponse;
import com.harpsharp.infra_rds.dto.response.ResponseWithData;
import jakarta.validation.constraints.NotNull;
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
    private final JwtUtil jwtUtil;

    @GetMapping("/board/posts/{postId}/comments")
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

    @GetMapping("/board/posts/{postId}/comments/{commentId}")
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

    @PostMapping("/board/posts/{postId}/comments")
    public ResponseEntity<ResponseWithData<Map<Long, ResponseCommentDTO>>> addComment(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody RequestCommentDTO commentDTO) throws IllegalAccessException {

        if(!isValid(accessToken, commentDTO.username()))
            throw new IllegalAccessException("INVALID_ACCESS");

        Map<Long, ResponseCommentDTO> object = commentService.save(commentDTO);

        ResponseWithData<Map<Long, ResponseCommentDTO>> apiResponse
                = new ResponseWithData<>(
                LocalDateTime.now(),
                HttpStatus.CREATED.value(),
                "ADD_COMMNET_SUCCESSFULLY",
                "댓글이 성공적으로 작성되었습니다.",
                object);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(apiResponse);

    }

    @PatchMapping("/board/posts/{postId}/comments/{commentId}")
    public ResponseEntity<ResponseWithData<Map<Long, ResponseCommentDTO>>> updateComment(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody RequestUpdateCommentDTO updatedComment) throws IllegalAccessException {

        if(!isValid(accessToken, updatedComment.username()))
            throw new IllegalAccessException("INVALID_ACCESS");

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


    @DeleteMapping("/board/posts/{postId}/comments/{commentId}")
    public ResponseEntity<ApiResponse> deleteComment(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody RequestCommentDTO commentDTO,
            @PathVariable Long commentId) throws IllegalAccessException {

        if(!isValid(accessToken, commentDTO.username()))
            throw new IllegalAccessException("INVALID_ACCESS");

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

    @NotNull
    private Boolean isValid(String accessToken, String username) {
        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            return false;
        }

        accessToken = accessToken.substring("Bearer ".length());

        return username.equals(jwtUtil.getUsername(accessToken));
    }
}
