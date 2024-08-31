package com.harpsharp.board.service;

import com.harpsharp.infra_rds.dto.board.like.RequestCommentLikeDTO;
import com.harpsharp.infra_rds.dto.board.like.RequestPostLikeDTO;
import com.harpsharp.infra_rds.dto.board.like.ResponseCommentLikeDTO;
import com.harpsharp.infra_rds.dto.board.like.ResponsePostLikeDTO;
import com.harpsharp.infra_rds.entity.board.Comment;
import com.harpsharp.infra_rds.entity.board.CommentLike;
import com.harpsharp.infra_rds.entity.board.Post;
import com.harpsharp.infra_rds.entity.board.PostLike;
import com.harpsharp.infra_rds.entity.user.User;
import com.harpsharp.infra_rds.repository.CommentLikeRepository;
import com.harpsharp.infra_rds.repository.CommentRepository;
import com.harpsharp.infra_rds.repository.PostLikeRepository;
import com.harpsharp.infra_rds.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public ResponseCommentLikeDTO triggerLike(RequestCommentLikeDTO requestCommentLikeDTO){
        Optional<CommentLike> commentLike = commentLikeRepository.findByUsernameAndPostId(requestCommentLikeDTO.username(), requestCommentLikeDTO.commentId());

        if(commentLike.isEmpty()){
            Long likeCount = commentService.likePost(requestCommentLikeDTO);

            User user = userRepository.findByUsername(requestCommentLikeDTO.username()).get();
            Comment comment = commentRepository.findById(requestCommentLikeDTO.commentId()).get();

            CommentLike newCommentLike = CommentLike
                    .builder()
                    .user(user)
                    .comment(comment)
                    .build();
            
            commentLikeRepository.save(newCommentLike);

            return new ResponseCommentLikeDTO(requestCommentLikeDTO.username(), requestCommentLikeDTO.commentId(), likeCount);
        }

        Long likeCount = commentService.unlikePost(requestCommentLikeDTO);
        commentLikeRepository.delete(commentLike.get());

        return new ResponseCommentLikeDTO(requestCommentLikeDTO.username(), requestCommentLikeDTO.commentId(), likeCount);
    }
}
