package com.harpsharp.infra_rds.mapper;

import com.harpsharp.infra_rds.dto.todo.RequestTodoCommentDTO;
import com.harpsharp.infra_rds.dto.todo.ResponseTodoCommentDTO;
import com.harpsharp.infra_rds.entity.TodoComment;
import com.harpsharp.infra_rds.entity.TodoPost;
import com.harpsharp.infra_rds.repository.TodoPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TodoCommentMapper {
    private final TodoPostRepository todoPostRepository;

    public TodoComment requestToEntity(RequestTodoCommentDTO requestTodoCommentDTO) {
        TodoPost todoPost = todoPostRepository
                .findById(requestTodoCommentDTO.todoPostId())
                .orElseThrow(() -> new IllegalArgumentException("TODO_POST_NOT_FOUND"));

        return new TodoComment(
                requestTodoCommentDTO.content(),
                todoPost.getUser(),
                todoPost
        );
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
        return todoComments.stream()
                .map(this::entityToResponseDTO)
                .collect(Collectors.toList());
    }

    public Map<Long, ResponseTodoCommentDTO> toMap(List<TodoComment> todoComments) {
        return todoComments.stream().collect(
                Collectors.toMap(TodoComment::getTodoCommentId, this::entityToResponseDTO));
    }

    public Map<Long,ResponseTodoCommentDTO> toMap(TodoComment todoComment) {
        Map<Long, ResponseTodoCommentDTO> object = new HashMap<>();
        object.put(todoComment.getTodoCommentId(), entityToResponseDTO(todoComment));
        return object;
    }

}
