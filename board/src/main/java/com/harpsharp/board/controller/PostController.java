package com.harpsharp.board.controller;

import com.harpsharp.auth.jwt.JwtUtil;
import com.harpsharp.infra_rds.dto.board.RequestPostDTO;
import com.harpsharp.infra_rds.dto.board.RequestUpdatePostDTO;
import com.harpsharp.infra_rds.dto.board.ResponsePostDTO;
import com.harpsharp.infra_rds.dto.response.ApiResponse;
import com.harpsharp.board.service.PostService;
import com.harpsharp.infra_rds.dto.response.ResponseWithData;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final JwtUtil jwtUtil;

    @GetMapping("/board/posts")
    public ResponseEntity<?> getAllPosts() {
        Map<Long, ResponsePostDTO> allPosts = postService.getAllPosts();

        ResponseWithData<Map<Long, ResponsePostDTO>> apiResponse =
                new ResponseWithData<>(
                        LocalDateTime.now(),
                        HttpStatus.OK,
                        "GET_ALL_POST_SUCCESSFULLY",
                        "모든 게시글이 정상적으로 조회되었습니다.",
                        allPosts);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }

    @PostMapping("/board/posts")
    public ResponseEntity<ApiResponse> savePost(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody RequestPostDTO requestPostDTO) {

        if(!isValid(accessToken, requestPostDTO.username()))
            throw new IllegalArgumentException("INVALID_ACCESS");

        Long savedId = postService.savePost(requestPostDTO);

        String redirectURI = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .replacePath("/board/posts/"+savedId.toString())
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectURI));
        System.out.println("redirectURI = " + redirectURI);
        ApiResponse apiResponse = new ApiResponse(
                LocalDateTime.now(),
                HttpStatus.SEE_OTHER,
                "REDIERCT_TO_ROOT",
                "게시글이 성공적으로 작성되었습니다. 해당 글로 이동합니다.");

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
                        LocalDateTime.now(),
                        HttpStatus.OK,
                        "GET_POST_SUCCESSFULLY",
                        "해당 게시글이 정상적으로 조회되었습니다.",
                        responsePostDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }

    @PatchMapping("/board/posts/{postId}")
    public ResponseEntity<ApiResponse> updatePost(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable Long postId,
            @RequestBody RequestPostDTO requestPostDTO) {

        if(!isValid(accessToken, requestPostDTO.username()))
            throw new IllegalArgumentException("INVALID_ACCESS");

        RequestUpdatePostDTO updated =
                new RequestUpdatePostDTO(
                        postId,
                        requestPostDTO.username(),
                        requestPostDTO.title(),
                        requestPostDTO.content());

        postService.updatePost(updated);

        String redirectURI = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .replacePath("/board/posts/" + postId.toString())
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectURI));
        System.out.println("redirectURI = " + redirectURI);
        ApiResponse apiResponse = new ApiResponse(
                LocalDateTime.now(),
                HttpStatus.SEE_OTHER,
                "REDIERCT_TO_ROOT",
                "게시글이 성공적으로 수정되었습니다.");

        return ResponseEntity
                .status(HttpStatus.SEE_OTHER)
                .headers(headers)
                .body(apiResponse);
    }

    @DeleteMapping("/board/posts/{postId}")
    public ResponseEntity<ApiResponse> deletePost(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable Long postId,
            @RequestBody RequestPostDTO requestPostDTO) {

        if(!isValid(accessToken, requestPostDTO.username()))
            throw new IllegalArgumentException("INVALID_ACCESS");

        postService.deletePost(postId);

        String redirectURI = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .replacePath("/board/posts")
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectURI));
        System.out.println("redirectURI = " + redirectURI);
        ApiResponse apiResponse = new ApiResponse(
                LocalDateTime.now(),
                HttpStatus.SEE_OTHER,
                "REDIERCT_TO_ROOT",
                "게시글이 성공적으로 삭제되었습니다.");

        return ResponseEntity
                .status(HttpStatus.SEE_OTHER)
                .headers(headers)
                .body(apiResponse);
    }

    @NotNull
    private Boolean isValid(String accessToken, String username) {
        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            System.out.println("invalid access");
            throw new IllegalArgumentException("Invalid access");
        }

        accessToken = accessToken.substring("Bearer ".length());


        return username.equals(jwtUtil.getUsername(accessToken));
    }
}
