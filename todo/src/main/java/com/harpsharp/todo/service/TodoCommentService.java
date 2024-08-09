package com.harpsharp.todo.service;

import com.harpsharp.infra_rds.dto.todo.RequestTodoCommentDTO;
import com.harpsharp.infra_rds.dto.todo.ResponseTodoCommentDTO;
import com.harpsharp.infra_rds.entity.TodoComment;
import com.harpsharp.infra_rds.mapper.TodoCommentMapper;
import com.harpsharp.infra_rds.repository.TodoCommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TodoCommentService {

    private final TodoCommentRepository commentRepository;
    private final TodoCommentMapper commentMapper;

    public List<ResponseTodoCommentDTO> getAllComments() {
        return commentRepository.findAll().stream()
                .map(commentMapper::entityToResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<ResponseTodoCommentDTO> getCommentById(Long id) {
        return commentRepository.findById(id)
                .map(commentMapper::entityToResponseDTO);
    }

    public ResponseTodoCommentDTO createComment(RequestTodoCommentDTO commentDTO) {
        TodoComment comment = commentMapper.requestToEntity(commentDTO);
        return commentMapper.entityToResponseDTO(commentRepository.save(comment));
    }

    public ResponseTodoCommentDTO updateComment(Long id, RequestTodoCommentDTO commentDTO) {
        TodoComment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid comment Id:" + id));
        comment.setContent(commentDTO.content());
        return commentMapper.entityToResponseDTO(commentRepository.save(comment));
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
