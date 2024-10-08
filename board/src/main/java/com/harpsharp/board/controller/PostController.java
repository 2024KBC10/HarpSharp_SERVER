package com.harpsharp.board.controller;

import com.harpsharp.infra_rds.dto.board.post.RequestPostDTO;
import com.harpsharp.infra_rds.dto.board.post.RequestUpdatePostDTO;
import com.harpsharp.infra_rds.dto.board.post.ResponsePostDTO;
import com.harpsharp.infra_rds.dto.response.ApiResponse;
import com.harpsharp.board.service.PostService;
import com.harpsharp.infra_rds.dto.response.ResponseWithData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/api/v1/board/posts")
    public ResponseEntity<?> getAllPosts() {
        Map<Long, ResponsePostDTO> allPosts = postService.getAllPosts();

        ResponseWithData<Map<Long, ResponsePostDTO>> apiResponse =
                new ResponseWithData<>(
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        "GET_ALL_POST_SUCCESSFULLY",
                        "모든 게시글이 정상적으로 조회되었습니다.",
                        allPosts);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }

    @PostMapping("/api/v1/board/posts")
    public ResponseEntity<ResponseWithData<?>> savePost(
            @RequestBody RequestPostDTO requestPostDTO) {

        Map<Long, ResponsePostDTO> savedPost = postService.savePost(requestPostDTO);

        ResponseWithData<Map<Long, ResponsePostDTO>> apiResponse
                = new ResponseWithData<>(
                LocalDateTime.now(),
                HttpStatus.CREATED.value(),
                "REDIERCT_TO_ROOT",
                "게시글이 성공적으로 작성되었습니다.",
                savedPost);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }

    @GetMapping("/api/v1/board/posts/{postId}")
    public ResponseEntity<ResponseWithData<Map<Long,ResponsePostDTO>>> getPostById(@PathVariable Long postId) {

        Map<Long,ResponsePostDTO> responsePostDTO = postService.getPostById(postId);

        ResponseWithData<Map<Long,ResponsePostDTO>> apiResponse =
                new ResponseWithData<>(
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        "GET_POST_SUCCESSFULLY",
                        "해당 게시글이 정상적으로 조회되었습니다.",
                        responsePostDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }

    @PatchMapping("/api/v1/board/posts")
    public ResponseEntity<ResponseWithData<Map<Long, ResponsePostDTO>>> updatePost(
            @RequestBody RequestUpdatePostDTO requestPostDTO) {

        RequestUpdatePostDTO updated =
                new RequestUpdatePostDTO(
                        requestPostDTO.postId(),
                        requestPostDTO.username(),
                        requestPostDTO.title(),
                        requestPostDTO.content());

        Map<Long, ResponsePostDTO> object = postService.updatePost(updated);

        ResponseWithData<Map<Long,ResponsePostDTO>> apiResponse
                = new ResponseWithData<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "REDIERCT_TO_ROOT",
                "게시글이 성공적으로 수정되었습니다.",
                object);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }

    @DeleteMapping("/api/v1/board/posts")
    public ResponseEntity<ApiResponse> deletePost(
            @RequestBody RequestUpdatePostDTO requestPostDTO) {

        Long postId = requestPostDTO.postId();

        postService.deletePost(postId);

        ApiResponse apiResponse = new ApiResponse(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "REDIERCT_TO_ROOT",
                "게시글이 성공적으로 삭제되었습니다.");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }
}
