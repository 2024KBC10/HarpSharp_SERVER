package com.harpsharp.board.controller;

import com.harpsharp.board.service.CommentLikeService;
import com.harpsharp.board.service.PostLikeService;
import com.harpsharp.infra_rds.dto.board.like.RequestCommentLikeDTO;
import com.harpsharp.infra_rds.dto.board.like.RequestPostLikeDTO;
import com.harpsharp.infra_rds.dto.board.like.ResponseCommentLikeDTO;
import com.harpsharp.infra_rds.dto.board.like.ResponsePostLikeDTO;
import com.harpsharp.infra_rds.dto.response.ResponseWithData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LikesController {
    private final PostLikeService postLikeService;
    private final CommentLikeService commentLikeService;

    @PostMapping("/api/v1/board/posts/likes")
    public ResponseEntity<ResponseWithData<ResponsePostLikeDTO>> triggerPostLike(@RequestBody RequestPostLikeDTO requestPostLikeDTO) {
        ResponsePostLikeDTO responsePostLikeDTO = postLikeService.triggerLike(requestPostLikeDTO);
        ResponseWithData<ResponsePostLikeDTO> apiResponse
                = new ResponseWithData<>(
                LocalDateTime.now(),
                HttpStatus.CREATED.value(),
                "UPDATED_POST_LIKES",
                "해당 포스트의 좋아요 요청이 정상적으로 처리되었습니다.",
                responsePostLikeDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }

    @PostMapping("/api/v1/board/comments/likes")
    public ResponseEntity<ResponseWithData<ResponseCommentLikeDTO>> triggerCommentLike(@RequestBody RequestCommentLikeDTO requestCommentLikeDTO) {
        ResponseCommentLikeDTO responseCommentLikeDTO = commentLikeService.triggerLike(requestCommentLikeDTO);
        ResponseWithData<ResponseCommentLikeDTO> apiResponse
                = new ResponseWithData<>(
                LocalDateTime.now(),
                HttpStatus.CREATED.value(),
                "UPDATED_POST_LIKES",
                "해당 댓글의 좋아요 요청이 정상적으로 처리되었습니다.",
                responseCommentLikeDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }
}
