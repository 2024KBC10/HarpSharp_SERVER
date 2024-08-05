package com.harpsharp.board.controller;
import com.harpsharp.auth.jwt.JwtUtil;
import com.harpsharp.auth.service.UserService;
import com.harpsharp.infra_rds.dto.board.RequestCommentDTO;
import com.harpsharp.board.service.CommentService;
import com.harpsharp.board.service.PostService;
import com.harpsharp.infra_rds.dto.response.ApiResponse;
import com.harpsharp.infra_rds.entity.Comment;
import com.harpsharp.infra_rds.entity.Post;
import com.harpsharp.infra_rds.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/board/posts/{postId}/comments")
    public ResponseEntity<ApiResponse> addComment(@PathVariable Long postId,
                                                  HttpServletRequest request,
                                                  @RequestHeader("Authorization") String accessToken,
                                                  @RequestBody RequestCommentDTO updatedComment) throws IllegalAccessException {
        String username = updatedComment.username();

        if(!username.equals(jwtUtil.getUsername(accessToken))) throw new IllegalAccessException("Invalid Access");

        User user = userService.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("Invalid Username:" + username));
        Post post = postService.getPostById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + postId));

        Comment comment = Comment
                .builder()
                .user(user)
                .post(post)
                .content(updatedComment.content())
                .build();

        commentService.save(comment);

        String host = request.getHeader("Host");
        String scheme = request.getHeader("X-Forwarded-Proto");
        String redirectURI = scheme + "://" + host + "/board/" + postId;

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectURI));

        return new ResponseEntity<>(headers, HttpStatus.TEMPORARY_REDIRECT);
    }

    @PutMapping("/board/posts/{postId}/comments/{commentId}")
    public ResponseEntity<ApiResponse> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            HttpServletRequest request,
            @RequestBody RequestCommentDTO updatedComment) {

        Comment existingComment = commentService.getCommentById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid comment Id:" + commentId));

        existingComment = existingComment
                .toBuilder()
                .content(updatedComment.content())
                .build();

        commentService.save(existingComment);

        String host = request.getHeader("Host");
        String scheme = request.getHeader("X-Forwarded-Proto");
        String redirectURI = scheme + "://" + host + "/board/" + postId;

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectURI));

        return new ResponseEntity<>(headers, HttpStatus.TEMPORARY_REDIRECT);
    }


    @DeleteMapping("/board/posts/{postId}/comments/{commentId}")
    public ResponseEntity<ApiResponse> deleteComment(@PathVariable Long postId,
                                @PathVariable Long commentId,
                                HttpServletRequest request) {

        commentService.deleteComment(commentId);

        String host   = request.getHeader("Host");
        String scheme = request.getHeader("X-Forwarded-Proto");
        String redirectURI = scheme + "://" + host + "/board/" + postId;

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectURI));

        return new ResponseEntity<>(headers, HttpStatus.TEMPORARY_REDIRECT);
    }
}
