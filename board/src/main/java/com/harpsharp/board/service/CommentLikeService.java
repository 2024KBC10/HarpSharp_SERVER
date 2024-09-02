package com.harpsharp.board.service;

import com.harpsharp.infra_rds.dto.board.like.RequestCommentLikeDTO;
import com.harpsharp.infra_rds.dto.board.like.ResponseCommentLikeDTO;
import com.harpsharp.infra_rds.entity.board.Comment;
import com.harpsharp.infra_rds.entity.board.CommentLike;
import com.harpsharp.infra_rds.entity.user.User;
import com.harpsharp.infra_rds.repository.CommentLikeRepository;
import com.harpsharp.infra_rds.repository.CommentRepository;
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
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public ResponseCommentLikeDTO triggerLike(RequestCommentLikeDTO requestCommentLikeDTO){
        Optional<CommentLike> commentLike = commentLikeRepository.findByUsernameAndCommentId(requestCommentLikeDTO.username(), requestCommentLikeDTO.commentId());
        Comment comment = commentRepository.findById(requestCommentLikeDTO.commentId()).get();

        if(commentLike.isEmpty()){
            User user = userRepository.findByUsername(requestCommentLikeDTO.username()).get();

            CommentLike newCommentLike = CommentLike
                    .builder()
                    .user(user)
                    .comment(comment)
                    .build();

            comment.addLike(newCommentLike);
            commentRepository.save(comment);

            return new ResponseCommentLikeDTO(requestCommentLikeDTO.username(), requestCommentLikeDTO.commentId(), comment.getLikes());
        }

        comment.removeLike(commentLike.get());
        commentRepository.save(comment);

        return new ResponseCommentLikeDTO(requestCommentLikeDTO.username(), requestCommentLikeDTO.commentId(), comment.getLikes());
    }
}
