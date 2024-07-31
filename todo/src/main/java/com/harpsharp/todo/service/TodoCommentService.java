package com.harpsharp.todo.service;

import com.harpsharp.infra_rds.entity.TodoComment;
import com.harpsharp.infra_rds.repository.TodoCommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TodoCommentService {

    private TodoCommentRepository commentRepository;

    public List<TodoComment> getAllComments() {
        return commentRepository.findAll();
    }

    public Optional<TodoComment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    public TodoComment createComment(TodoComment comment) {
        return commentRepository.save(comment);
    }

    public TodoComment updateComment(Long id, TodoComment commentDetails) {
        TodoComment comment = commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid comment Id:" + id));
        comment.setContent(commentDetails.getContent());
        return commentRepository.save(comment);
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
