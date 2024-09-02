package com.harpsharp.board.service;

import com.harpsharp.infra_rds.dto.board.like.RequestCommentLikeDTO;
import com.harpsharp.infra_rds.dto.board.like.ResponseCommentLikeDTO;
import com.harpsharp.infra_rds.entity.board.Comment;
import com.harpsharp.infra_rds.entity.board.CommentLike;
import com.harpsharp.infra_rds.entity.user.User;
import com.harpsharp.infra_rds.repository.CommentLikeRepository;
import com.harpsharp.infra_rds.repository.CommentRepository;
import com.harpsharp.infra_rds.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentLikeService {
    private final CommentLikeRepository commentLikeRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @PersistenceContext
    private final EntityManager em;

    @Retryable(
            retryFor = {ObjectOptimisticLockingFailureException.class},
            maxAttempts = 1000,
            backoff = @Backoff(100)
    )
    public ResponseCommentLikeDTO triggerLike(RequestCommentLikeDTO requestCommentLikeDTO){
        Optional<CommentLike> commentLike = commentLikeRepository.findByUsernameAndCommentId(requestCommentLikeDTO.username(), requestCommentLikeDTO.commentId());
        Comment comment = commentRepository
                .findById(requestCommentLikeDTO.commentId())
                .orElseThrow(()->new IllegalArgumentException("USER_NOT_FOUND"));

        if(commentLike.isEmpty()){
            User user = userRepository
                    .findByUsername(requestCommentLikeDTO.username())
                    .orElseThrow(()->new IllegalArgumentException("USER_NOT_FOUND"));

            CommentLike newCommentLike = CommentLike
                    .builder()
                    .user(user)
                    .comment(comment)
                    .build();

            comment.addLike(newCommentLike);
            commentLikeRepository.save(newCommentLike);
            Comment update = commentRepository.save(comment);
            em.flush();


            return new ResponseCommentLikeDTO(requestCommentLikeDTO.username(), requestCommentLikeDTO.commentId(), update.getLikes());
        }

        comment.removeLike(commentLike.get());
        commentLikeRepository.delete(commentLike.get());
        Comment update = commentRepository.save(comment);
        em.flush();

        return new ResponseCommentLikeDTO(requestCommentLikeDTO.username(), requestCommentLikeDTO.commentId(), update.getLikes());
    }
}
