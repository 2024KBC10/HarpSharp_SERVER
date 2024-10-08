package com.harpsharp.infra_rds.mapper;

import com.harpsharp.infra_rds.dto.todo.post.RequestTodoPostDTO;
import com.harpsharp.infra_rds.dto.todo.post.ResponseTodoPostDTO;
import com.harpsharp.infra_rds.entity.todo.TodoPost;
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
public class TodoPostMapper {
    private final UserRepository userRepository;
    private final TodoCommentMapper todoCommentMapper;

    public TodoPost requestToEntity(RequestTodoPostDTO requestTodoPostDTO) {
        User user = userRepository
                .findByUsername(requestTodoPostDTO.username())
                .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

        return TodoPost
                .builder()
                .user(user)
                .title(requestTodoPostDTO.title())
                .content(requestTodoPostDTO.content())
                .status(requestTodoPostDTO.status())
                .startAt(requestTodoPostDTO.startAt())
                .endAt(requestTodoPostDTO.endAt())
                .build();
    }

    public ResponseTodoPostDTO entityToResponseDTO(TodoPost todoPost) {
        return new ResponseTodoPostDTO(
                todoPost.getUser().getUsername(),
                todoPost.getTitle(),
                todoPost.getContent(),
                todoPost.getStatus(),
                todoPost.getStartAt(),
                todoPost.getEndAt(),
                todoPost.getCreatedAt(),
                todoPost.getUpdatedAt(),
                todoCommentMapper.toMap(todoPost.getTodoComments())
        );
    }

    public List<ResponseTodoPostDTO> convertPostsToResponse(List<TodoPost> todoPosts) {
        if(todoPosts == null || todoPosts.isEmpty()) return new ArrayList<>();
        return todoPosts.stream()
                .map(this::entityToResponseDTO)
                .collect(Collectors.toList());
    }

    public Map<Long, ResponseTodoPostDTO> toMap(List<TodoPost> todoPosts) {
        if(todoPosts == null) return new HashMap<>();

        return todoPosts.stream().collect(
                Collectors.toMap(TodoPost::getTodoId, this::entityToResponseDTO));
    }

    public Map<Long, ResponseTodoPostDTO> toMap(TodoPost todoPost) {
        Map<Long, ResponseTodoPostDTO> object = new HashMap<>();
        object.put(todoPost.getTodoId(), entityToResponseDTO(todoPost));

        return object;
    }
}
