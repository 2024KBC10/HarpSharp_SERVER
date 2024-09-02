package com.harpsharp.board.service;

import com.harpsharp.infra_rds.dto.board.like.RequestPostLikeDTO;
import com.harpsharp.infra_rds.dto.board.like.ResponsePostLikeDTO;
import com.harpsharp.infra_rds.entity.board.Post;
import com.harpsharp.infra_rds.entity.board.PostLike;
import com.harpsharp.infra_rds.entity.user.User;
import com.harpsharp.infra_rds.repository.PostLikeRepository;
import com.harpsharp.infra_rds.repository.PostRepository;
import com.harpsharp.infra_rds.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Retryable(
            retryFor = {ObjectOptimisticLockingFailureException.class},
            maxAttempts = 1000,
            backoff = @Backoff(100)
    )
    public ResponsePostLikeDTO triggerLike(RequestPostLikeDTO requestPostLikeDTO){
        Optional<PostLike> postLike = postLikeRepository.findByUsernameAndPostId(requestPostLikeDTO.username(), requestPostLikeDTO.postId());
        Post post = postRepository
                .findById(requestPostLikeDTO.postId())
                .orElseThrow(() -> new IllegalArgumentException("POST_NOT_FOUND"));

        if(postLike.isEmpty()){
            User user = userRepository
                    .findByUsername(requestPostLikeDTO.username())
                    .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

            PostLike newPostLike = PostLike
                    .builder()
                    .user(user)
                    .post(post)
                    .build();

            post.addLike(newPostLike);
            Post update = postRepository.save(post);

            return new ResponsePostLikeDTO(requestPostLikeDTO.username(), requestPostLikeDTO.postId(), update.getLikes());
        }

        post.removeLike(postLike.get());
        Post update = postRepository.save(post);

        return new ResponsePostLikeDTO(requestPostLikeDTO.username(), requestPostLikeDTO.postId(), update.getLikes());
    }
}
