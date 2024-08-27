package com.harpsharp.board.service;

import com.harpsharp.infra_rds.dto.board.RequestPostDTO;
import com.harpsharp.infra_rds.dto.board.RequestUpdatePostDTO;
import com.harpsharp.infra_rds.dto.board.ResponsePostDTO;
import com.harpsharp.infra_rds.entity.board.Post;
import com.harpsharp.infra_rds.mapper.PostMapper;
import com.harpsharp.infra_rds.repository.CommentRepository;
import com.harpsharp.infra_rds.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final CommentRepository commentRepository;

    public Map<Long, ResponsePostDTO> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return postMapper.toMap(posts);
    }

    public Map<Long,ResponsePostDTO> getPostById(Long id) {
        Post post = postRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("POST_NOT_FOUND"));
        return postMapper.toMap(post);
    }

    public Map<Long,ResponsePostDTO> savePost(RequestPostDTO requestPostDTO) {
        Post post = postRepository.save(postMapper.requestToEntity(requestPostDTO));
        return postMapper.toMap(post);
    }

    public Map<Long,ResponsePostDTO> updatePost(RequestUpdatePostDTO updatedPostDTO) {
        Long postId = updatedPostDTO.postId();


        Post existedPost = postRepository
                .findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("POST_NOT_FOUND"));

        Post updatedPost = existedPost
                    .toBuilder()
                    .title(updatedPostDTO.title())
                    .content(updatedPostDTO.content())
                    .build();

        postRepository.save(updatedPost);
        return postMapper.toMap(updatedPost);
    }

    public void deletePost(Long id) {
        Post post = postRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("POST_NOT_FOUND"));
        commentRepository.deleteByPost(post);
        postRepository.deleteById(id);
    }

    public void clear(){
        postRepository.deleteAll();
    }
}
