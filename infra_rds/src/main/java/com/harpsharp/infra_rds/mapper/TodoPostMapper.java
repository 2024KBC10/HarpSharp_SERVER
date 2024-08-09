package com.harpsharp.infra_rds.mapper;

import com.harpsharp.infra_rds.dto.todo.RequestTodoPostDTO;
import com.harpsharp.infra_rds.dto.todo.RequestUpdateTodoPostDTO;
import com.harpsharp.infra_rds.dto.todo.ResponseTodoPostDTO;
import com.harpsharp.infra_rds.entity.TodoPost;
import com.harpsharp.infra_rds.entity.User;
import com.harpsharp.infra_rds.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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

        return TodoPost.builder()
                .title(requestTodoPostDTO.title())
                .content(requestTodoPostDTO.content())
                .status(requestTodoPostDTO.status())
                .startAt(requestTodoPostDTO.startAt())
                .endAt(requestTodoPostDTO.endAt())
                .user(user)
                .build();
    }

    public TodoPost updateRequestToEntity(TodoPost todoPost, RequestUpdateTodoPostDTO updateTodoPostDTO) {
        todoPost.setTitle(updateTodoPostDTO.title());
        todoPost.setContent(updateTodoPostDTO.content());
        todoPost.setStatus(updateTodoPostDTO.status());
        todoPost.setStartAt(updateTodoPostDTO.startAt());
        todoPost.setEndAt(updateTodoPostDTO.endAt());
        return todoPost;
    }

    public ResponseTodoPostDTO entityToResponseDTO(TodoPost todoPost) {
        return new ResponseTodoPostDTO(
                todoPost.getTodoId(),
                todoPost.getTitle(),
                todoPost.getContent(),
                todoPost.getStatus(),
                todoPost.getStartAt(),
                todoPost.getEndAt(),
                todoPost.getLikes(),
                todoCommentMapper.convertCommentsToResponse(todoPost.getTodoComments())
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
}
