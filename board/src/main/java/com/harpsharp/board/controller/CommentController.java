package com.harpsharp.board.controller;
import com.harpsharp.auth.jwt.JwtUtil;
import com.harpsharp.infra_rds.dto.board.RequestCommentDTO;
import com.harpsharp.board.service.CommentService;
import com.harpsharp.infra_rds.dto.board.RequestUpdateCommnetDTO;
import com.harpsharp.infra_rds.dto.board.ResponseCommentDTO;
import com.harpsharp.infra_rds.dto.board.ResponsePostDTO;
import com.harpsharp.infra_rds.dto.response.ApiResponse;
import com.harpsharp.infra_rds.dto.response.ResponseWithData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final JwtUtil jwtUtil;

    @PostMapping("/board/posts/{postId}/comments")
    public ResponseEntity<ResponseWithData<Map<Long, ResponseCommentDTO>>> addComment(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody RequestCommentDTO commentDTO) throws IllegalAccessException {

        Map<Long, ResponseCommentDTO> object = commentService.save(commentDTO);

        if(!isValid(accessToken, commentDTO.username()))
            throw new IllegalAccessException("INVALID_ACCESS");

        ResponseWithData<Map<Long, ResponseCommentDTO>> apiResponse
                = new ResponseWithData<>(
                LocalDateTime.now(),
                HttpStatus.CREATED.value(),
                "ADD_COMMNET_SUCCESSFULLY",
                "댓글이 성공적으로 작성되었습니다.",
                object
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(apiResponse);

    }

    @PutMapping("/board/posts/{postId}/comments/{commentId}")
    public ResponseEntity<ResponseWithData<Map<Long, ResponseCommentDTO>>> updateComment(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody RequestUpdateCommnetDTO updatedComment) throws IllegalAccessException {

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
    public ResponseEntity<ApiResponse> deleteComment(@PathVariable Long postId,
                                @PathVariable Long commentId,
                                HttpServletRequest request) {

        commentService.deleteComment(commentId);

        ApiResponse apiResponse = new ApiResponse(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "UPDATE_COMMNET_SUCCESSFULLY",
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
