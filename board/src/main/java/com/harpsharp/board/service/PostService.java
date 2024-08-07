package com.harpsharp.board.service;

import com.harpsharp.infra_rds.dto.board.RequestPostDTO;
import com.harpsharp.infra_rds.dto.board.RequestUpdatePostDTO;
import com.harpsharp.infra_rds.dto.board.ResponsePostDTO;
import com.harpsharp.infra_rds.entity.Post;
import com.harpsharp.infra_rds.mapper.PostMapper;
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

    public Map<Long, ResponsePostDTO> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return postMapper.toMap(posts);
    }

    public ResponsePostDTO getPostById(Long id) {
        Post post = postRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("POST_NOT_FOUND"));

        return postMapper.postToResponseDTO(post);
    }

    public void savePost(RequestPostDTO requestPostDTO) {
        postRepository.save(postMapper.requestToEntity(requestPostDTO));
    }

    public void updatePost(RequestUpdatePostDTO updatedPostDTO) {
        Long postId = updatedPostDTO.postId();


        Post existedPost = postRepository
                .findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("POST_NOT_FOUND"));

        existedPost = existedPost
                    .toBuilder()
                    .title(updatedPostDTO.title())
                    .content(updatedPostDTO.content())
                    .build();

        postRepository.save(existedPost);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}
