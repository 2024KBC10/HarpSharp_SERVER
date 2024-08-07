package com.harpsharp.board.controller;
import com.harpsharp.infra_rds.dto.board.RequestCommentDTO;
import com.harpsharp.board.service.CommentService;
import com.harpsharp.infra_rds.dto.board.RequestUpdateCommnetDTO;
import com.harpsharp.infra_rds.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/board/posts/{postId}/comments")
    public ResponseEntity<ApiResponse> addComment(@PathVariable Long postId,
                                                  HttpServletRequest request,
                                                  @RequestHeader("Authorization") String accessToken,
                                                  @RequestBody RequestCommentDTO commentDTO) throws IllegalAccessException {

        commentService.save(commentDTO);

        String redirectURI = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .replacePath("/board/" + postId.toString())
                .toUriString();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectURI));

        ApiResponse apiResponse = new ApiResponse(
                "ADD_COMMNET_SUCCESSFULLY",
                "댓글이 성공적으로 작성되었습니다.");
        return ResponseEntity
                .status(HttpStatus.TEMPORARY_REDIRECT)
                .headers(headers)
                .body(apiResponse);

    }

    @PutMapping("/board/posts/{postId}/comments/{commentId}")
    public ResponseEntity<ApiResponse> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            HttpServletRequest request,
            @RequestBody RequestUpdateCommnetDTO updatedComment) {

        commentService.updateComment(updatedComment);

        String host = request.getHeader("Host");
        String redirectURI = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .replacePath("/board/" + postId.toString())
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectURI));

        ApiResponse apiResponse = new ApiResponse(
                "UPDATE_COMMNET_SUCCESSFULLY",
                "댓글이 성공적으로 수정되었습니다.");

        return ResponseEntity
                .status(HttpStatus.TEMPORARY_REDIRECT)
                .headers(headers)
                .body(apiResponse);
    }


    @DeleteMapping("/board/posts/{postId}/comments/{commentId}")
    public ResponseEntity<ApiResponse> deleteComment(@PathVariable Long postId,
                                @PathVariable Long commentId,
                                HttpServletRequest request) {

        commentService.deleteComment(commentId);

        String host   = request.getHeader("Host");
        String redirectURI = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .replacePath("/board/" + postId.toString())
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectURI));

        ApiResponse apiResponse = new ApiResponse(
                "UPDATE_COMMNET_SUCCESSFULLY",
                "댓글이 성공적으로 삭제되었습니다.");

        return ResponseEntity
                .status(HttpStatus.TEMPORARY_REDIRECT)
                .headers(headers)
                .body(apiResponse);
    }
}
