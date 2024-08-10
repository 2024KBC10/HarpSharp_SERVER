package com.harpsharp.infra_rds.mapper;

import com.harpsharp.infra_rds.dto.todo.RequestTodoPostDTO;
import com.harpsharp.infra_rds.dto.todo.ResponseTodoPostDTO;
import com.harpsharp.infra_rds.entity.TodoPost;
import com.harpsharp.infra_rds.entity.User;
import com.harpsharp.infra_rds.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
                .content_hint(requestTodoPostDTO.content_hint())
                .content_goal(requestTodoPostDTO.content_goal())
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
                todoPost.getContent_hint(),
                todoPost.getContent_goal(),
                todoPost.getStatus(),
                todoPost.getStartAt(),
                todoPost.getEndAt(),
                todoPost.getCreatedAt(),
                todoPost.getUpdatedAt(),
                todoPost.getLikes(),
                todoCommentMapper.toMap(todoPost.getTodoComments())
        );
    }

    public List<ResponseTodoPostDTO> convertPostsToResponse(List<TodoPost> todoPosts) {
        return todoPosts.stream()
                .map(this::entityToResponseDTO)
                .collect(Collectors.toList());
    }

    public Map<Long, ResponseTodoPostDTO> toMap(List<TodoPost> todoPosts) {
        return todoPosts.stream().collect(
                Collectors.toMap(TodoPost::getTodoId, this::entityToResponseDTO));
    }

    public Map<Long, ResponseTodoPostDTO> toMap(TodoPost todoPost) {
        Map<Long, ResponseTodoPostDTO> object = new HashMap<>();
        object.put(todoPost.getTodoId(), entityToResponseDTO(todoPost));

        return object;
    }
}
