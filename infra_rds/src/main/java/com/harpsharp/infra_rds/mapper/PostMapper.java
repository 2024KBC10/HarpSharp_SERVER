package com.harpsharp.infra_rds.mapper;

import com.harpsharp.infra_rds.dto.board.ResponsePostDTO;
import com.harpsharp.infra_rds.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PostMapper {
    private final CommentMapper commentMapper;


    public ResponsePostDTO postToResponseDTO(Post post){
        return new ResponsePostDTO(
                post.getUsername(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                commentMapper.convertCommentsToResponse(post.getComments()));
    }

    public List<ResponsePostDTO> convertPostsToResponse(List<Post> posts) {
        return posts.stream() // 스트림 시작
                .map(this::postToResponseDTO) // 각 Post 객체에 postToDTO 함수 적용
                .collect(Collectors.toList()); // 결과를 List로 수집
    }

    public Map<Long, ResponsePostDTO> toMap(List<Post> posts) {
        return posts.stream().collect(
                Collectors.toMap(Post::getPostId, this::postToResponseDTO));
    }
}
