package com.harpsharp.infra_rds.mapper;

import com.harpsharp.infra_rds.dto.todo.comment.RequestTodoCommentDTO;
import com.harpsharp.infra_rds.dto.todo.comment.ResponseTodoCommentDTO;
import com.harpsharp.infra_rds.entity.todo.TodoComment;
import com.harpsharp.infra_rds.entity.todo.TodoPost;
import com.harpsharp.infra_rds.entity.user.User;
import com.harpsharp.infra_rds.repository.TodoPostRepository;
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
public class TodoCommentMapper {
    private final TodoPostRepository todoPostRepository;
    private final UserRepository userRepository;

    public TodoComment requestToEntity(RequestTodoCommentDTO requestTodoCommentDTO) {
        TodoPost todoPost = todoPostRepository
                .findById(requestTodoCommentDTO.postId())
                .orElseThrow(() -> new IllegalArgumentException("TODO_POST_NOT_FOUND"));

        User user = userRepository
                .findByUsername(requestTodoCommentDTO.username())
                .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

        return TodoComment
                .builder()
                .todoPost(todoPost)
                .user(user)
                .content(requestTodoCommentDTO.content())
                .build();
    }

    public ResponseTodoCommentDTO entityToResponseDTO(TodoComment todoComment) {
        return new ResponseTodoCommentDTO(
                todoComment.getUser().getUsername(),
                todoComment.getContent(),
                todoComment.getCreatedAt(),
                todoComment.getUpdatedAt()
        );
    }

    public List<ResponseTodoCommentDTO> convertCommentsToResponse(List<TodoComment> todoComments) {
        if(todoComments == null) return new ArrayList<>();

        return todoComments.stream()
                .map(this::entityToResponseDTO)
                .collect(Collectors.toList());
    }

    public Map<Long, ResponseTodoCommentDTO> toMap(List<TodoComment> todoComments) {
        if(todoComments == null) return new HashMap<>();

        return todoComments.stream().collect(
                Collectors.toMap(TodoComment::getTodoCommentId, this::entityToResponseDTO));
    }

    public Map<Long,ResponseTodoCommentDTO> toMap(TodoComment todoComment) {
        Map<Long, ResponseTodoCommentDTO> object = new HashMap<>();
        object.put(todoComment.getTodoCommentId(), entityToResponseDTO(todoComment));
        return object;
    }

}
