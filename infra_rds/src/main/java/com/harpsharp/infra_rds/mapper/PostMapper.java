package com.harpsharp.infra_rds.mapper;

import com.harpsharp.infra_rds.dto.board.post.RequestPostDTO;
import com.harpsharp.infra_rds.dto.board.post.ResponsePostDTO;
import com.harpsharp.infra_rds.dto.todo.comment.ResponseTodoCommentDTO;
import com.harpsharp.infra_rds.entity.board.Post;
import com.harpsharp.infra_rds.entity.todo.TodoComment;
import com.harpsharp.infra_rds.entity.user.User;
import com.harpsharp.infra_rds.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PostMapper {
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;

    public Post requestToEntity(RequestPostDTO requestPostDTO) {
        User user = userRepository
                .findByUsername(requestPostDTO.username())
                .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

        return Post
                .builder()
                .user(user)
                .title(requestPostDTO.title())
                .content(requestPostDTO.content())
                .build();
    }

    public ResponsePostDTO postToResponseDTO(Post post){
        return new ResponsePostDTO(
                post.getUsername(),
                post.getTitle(),
                post.getContent(),
                post.getLikes(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                commentMapper.toMap(post.getComments()));
    }

    public List<ResponsePostDTO> convertPostsToResponse(List<Post> posts) {
        if(posts == null) return new ArrayList<>();

        return posts.stream() // 스트림 시작
                .map(this::postToResponseDTO) // 각 Post 객체에 postToDTO 함수 적용
                .collect(Collectors.toList()); // 결과를 List로 수집
    }

    public Map<Long, ResponsePostDTO> toMap(List<Post> posts) {
        if(posts == null) return new HashMap<>();

        return posts.stream().collect(
                Collectors.toMap(Post::getPostId, this::postToResponseDTO));
    }

    public Map<Long, ResponsePostDTO> toMap(Post post){
        Long postId = post.getPostId();
        Map<Long, ResponsePostDTO> object = new HashMap<>();
        object.put(postId, postToResponseDTO(post));
        return object;
    }
}
