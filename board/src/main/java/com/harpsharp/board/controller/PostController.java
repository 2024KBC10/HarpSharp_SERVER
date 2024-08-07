package com.harpsharp.board.controller;

import com.harpsharp.auth.service.UserService;
import com.harpsharp.infra_rds.dto.board.RequestPostDTO;
import com.harpsharp.infra_rds.dto.board.RequestUpdatePostDTO;
import com.harpsharp.infra_rds.dto.board.ResponsePostDTO;
import com.harpsharp.infra_rds.dto.response.ApiResponse;
import com.harpsharp.board.service.PostService;
import com.harpsharp.infra_rds.dto.response.ResponseWithData;
import com.harpsharp.infra_rds.mapper.PostMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final PostMapper postMapper;

    @GetMapping("/board/posts")
    public ResponseEntity<?> getAllPosts() {
        Map<Long, ResponsePostDTO> allPosts = postService.getAllPosts();

        ResponseWithData<Map<Long, ResponsePostDTO>> apiResponse =
                new ResponseWithData<>(
                        "GET_ALL_POST_SUCCESSFULLY",
                        "모든 게시글이 정상적으로 조회되었습니다.",
                        allPosts);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }

    @PostMapping("/board/posts")
    public ResponseEntity<ApiResponse> savePost(
            @RequestBody RequestPostDTO createdPost,
            HttpServletRequest request) {

        postService.savePost(createdPost);

        String host = request.getHeader("Host");
        String redirectURI = "https://" + host + "/board";

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectURI));
        System.out.println("redirectURI = " + redirectURI);
        ApiResponse apiResponse = new ApiResponse(
                "REDIERCT_TO_ROOT",
                "게시글이 성공적으로 작성되었습니다. 루트 페이지로 이동합니다.");

        return ResponseEntity
                .status(HttpStatus.SEE_OTHER)
                .headers(headers)
                .body(apiResponse);
    }

    @GetMapping("/board/posts/{postId}")
    public ResponseEntity<ResponseWithData<ResponsePostDTO>> getPostById(@PathVariable Long postId) {
        ResponsePostDTO responsePostDTO = postService.getPostById(postId);

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
                                                  @RequestBody RequestUpdatePostDTO updatedPost,
                                                  HttpServletRequest request) {


        postService.updatePost(updatedPost);

        String host = request.getHeader("Host");
        String redirectURI = "http://" + host + "/board/" + postId;

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectURI));
        System.out.println("redirectURI = " + redirectURI);
        ApiResponse apiResponse = new ApiResponse(
                "REDIERCT_TO_ROOT",
                "게시글이 성공적으로 수정되었습니다. 루트 페이지로 이동합니다.");

        return ResponseEntity
                .status(HttpStatus.SEE_OTHER)
                .headers(headers)
                .body(apiResponse);
    }

    @DeleteMapping("/board/posts/{id}")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable Long id,
                                                  @RequestBody RequestPostDTO deletedPost,
                                                  HttpServletRequest request) {
        postService.deletePost(id);

        String host = request.getHeader("Host");
        String redirectURI = "http://" + host + "/board/posts";

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectURI));
        System.out.println("redirectURI = " + redirectURI);
        ApiResponse apiResponse = new ApiResponse(
                "REDIERCT_TO_ROOT",
                "게시글이 성공적으로 삭제되었습니다. 루트 페이지로 이동합니다.");

        return ResponseEntity
                .status(HttpStatus.SEE_OTHER)
                .headers(headers)
                .body(apiResponse);
    }
}
