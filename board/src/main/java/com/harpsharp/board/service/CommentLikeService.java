package com.harpsharp.board.service;

import com.harpsharp.infra_rds.dto.board.like.RequestCommentLikeDTO;
import com.harpsharp.infra_rds.dto.board.like.RequestPostLikeDTO;
import com.harpsharp.infra_rds.dto.board.like.ResponseCommentLikeDTO;
import com.harpsharp.infra_rds.dto.board.like.ResponsePostLikeDTO;
import com.harpsharp.infra_rds.entity.board.CommentLike;
import com.harpsharp.infra_rds.entity.board.PostLike;
import com.harpsharp.infra_rds.repository.CommentLikeRepository;
import com.harpsharp.infra_rds.repository.PostLikeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentLikeService {
    private final CommentLikeRepository commentLikeRepository;
    private final CommentService commentService;

    public ResponseCommentLikeDTO triggerLike(RequestCommentLikeDTO requestCommentLikeDTO){
        Optional<CommentLike> commentLike = commentLikeRepository.findByUsernameAndPostId(requestCommentLikeDTO.username(), requestCommentLikeDTO.commentId());

        if(commentLike.isEmpty()){
            Long likeCount = commentService.likePost(requestCommentLikeDTO);
            return new ResponseCommentLikeDTO(requestCommentLikeDTO.username(), requestCommentLikeDTO.commentId(), likeCount);
        }

        Long likeCount = commentService.unlikePost(requestCommentLikeDTO);
        commentLikeRepository.delete(commentLike.get());

        return new ResponseCommentLikeDTO(requestCommentLikeDTO.username(), requestCommentLikeDTO.commentId(), likeCount);
    }
}
