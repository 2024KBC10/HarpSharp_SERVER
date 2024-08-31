package com.harpsharp.board.service;

import com.harpsharp.infra_rds.dto.board.like.RequestPostLikeDTO;
import com.harpsharp.infra_rds.dto.board.like.ResponsePostLikeDTO;
import com.harpsharp.infra_rds.entity.board.Post;
import com.harpsharp.infra_rds.entity.board.PostLike;
import com.harpsharp.infra_rds.entity.user.User;
import com.harpsharp.infra_rds.repository.PostLikeRepository;
import com.harpsharp.infra_rds.repository.PostRepository;
import com.harpsharp.infra_rds.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public ResponsePostLikeDTO triggerLike(RequestPostLikeDTO requestPostLikeDTO){
        Optional<PostLike> postLike = postLikeRepository.findByUsernameAndPostId(requestPostLikeDTO.username(), requestPostLikeDTO.postId());

        if(postLike.isEmpty()){
            User user = userRepository.findByUsername(requestPostLikeDTO.username()).get();
            Post post = postRepository.findById(requestPostLikeDTO.postId()).get();

            PostLike newPostLike = PostLike
                    .builder()
                    .user(user)
                    .post(post)
                    .build();

            post.incLikes();

            postRepository.save(post);
            postLikeRepository.save(newPostLike);
            Long likeCount = post.getLikes();

            return new ResponsePostLikeDTO(requestPostLikeDTO.username(), requestPostLikeDTO.postId(), likeCount);
        }

        Post post = postRepository.findById(requestPostLikeDTO.postId()).get();

        post.decLikes();
        postRepository.save(post);

        Long likeCount = post.getLikes();
        postLikeRepository.delete(postLike.get());

        return new ResponsePostLikeDTO(requestPostLikeDTO.username(), requestPostLikeDTO.postId(), likeCount);
    }
}
