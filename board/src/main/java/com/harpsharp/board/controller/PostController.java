package com.harpsharp.board.controller;

import com.harpsharp.auth.service.UserService;
import com.harpsharp.infra_rds.dto.board.RequestPostDTO;
import com.harpsharp.infra_rds.dto.board.ResponsePostDTO;
import com.harpsharp.infra_rds.dto.response.ApiResponse;
import com.harpsharp.board.service.PostService;
import com.harpsharp.infra_rds.dto.response.ResponseWithData;
import com.harpsharp.infra_rds.entity.Post;
import com.harpsharp.infra_rds.entity.User;
import com.harpsharp.infra_rds.mapper.PostMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final PostMapper postMapper;

    @GetMapping("/board/posts")
    public ResponseEntity<?> getAllPosts(Model model) {
        List<Post> allPosts = postService.getAllPosts();
        Map<Long, ResponsePostDTO> postsJson = postMapper.toMap(allPosts);

        ResponseWithData<Map<Long, ResponsePostDTO>> apiResponse =
                new ResponseWithData<>(
                        "GET_ALL_POST_SUCCESSFULLY",
                        "모든 게시글이 정상적으로 조회되었습니다.",
                        postsJson);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }

    @PostMapping("/board/posts")
    public ResponseEntity<ApiResponse> savePost(
            @RequestBody RequestPostDTO createdPost,
            HttpServletRequest request) {

        User author = userService
                .findByUsername(createdPost.username())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Username"));

        Post post = Post
                .builder()
                .user(author)
                .title(createdPost.title())
                .content(createdPost.content())
                .build();

        postService.savePost(post);

        String host = request.getHeader("Host");
        String scheme = request.getHeader("X-Forwarded-Proto");
        String redirectURI = "http://" + host + "/board";

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectURI));

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping("/board/posts/{postId}")
    public ResponseEntity<ResponseWithData<ResponsePostDTO>> getPostById(@PathVariable Long postId) {
        Post post = postService.getPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + postId));

        ResponsePostDTO responsePostDTO
                = postMapper.postToResponseDTO(post);

        ResponseWithData<ResponsePostDTO> apiResponse =
                new ResponseWithData<>(
                        "GET_POST_SUCCESSFULLY",
                        "해당 게시글이 정상적으로 조회되었습니다.",
                        responsePostDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }

    @PutMapping("/board/posts/{postId}")
    public ResponseEntity<ApiResponse> updatePost(@PathVariable Long postId,
                                                  @RequestBody RequestPostDTO updatedPost,
                                                  HttpServletRequest request) {

        User author = userService
                .findByUsername(updatedPost.username())
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + postId));

        Post existPost = postService
                .getPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + postId));

        existPost = existPost
                .toBuilder()
                .user(author)
                .title(updatedPost.title())
                .content(updatedPost.content())
                .build();

        postService.savePost(existPost);

        String host = request.getHeader("Host");
        String scheme = request.getHeader("X-Forwarded-Proto");
        String redirectURI = "http://" + host + "/board/" + postId;

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectURI));

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @DeleteMapping("/board/posts/{id}")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable Long id,
                                                  @RequestBody RequestPostDTO deletedPost,
                                                  HttpServletRequest request) {
        postService.deletePost(id);

        String host = request.getHeader("Host");
        String scheme = request.getHeader("X-Forwarded-Proto");
        String redirectURI = "http://" + host + "/board/posts";

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectURI));

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
