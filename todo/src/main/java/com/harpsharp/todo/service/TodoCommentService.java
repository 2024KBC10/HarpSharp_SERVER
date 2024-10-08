package com.harpsharp.todo.service;

import com.harpsharp.infra_rds.dto.todo.comment.RequestTodoCommentDTO;
import com.harpsharp.infra_rds.dto.todo.comment.RequestUpdateTodoCommentDTO;
import com.harpsharp.infra_rds.dto.todo.comment.ResponseTodoCommentDTO;
import com.harpsharp.infra_rds.entity.todo.TodoComment;
import com.harpsharp.infra_rds.entity.todo.TodoPost;
import com.harpsharp.infra_rds.mapper.TodoCommentMapper;
import com.harpsharp.infra_rds.repository.TodoCommentRepository;
import com.harpsharp.infra_rds.repository.TodoPostRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
@Transactional
@RequiredArgsConstructor
public class TodoCommentService {

    private final TodoCommentRepository commentRepository;
    private final TodoCommentMapper commentMapper;
    private final TodoPostRepository todoPostRepository;
    @PersistenceContext
    private final EntityManager em;



    public Map<Long, ResponseTodoCommentDTO> getAllComments() {
        return commentMapper.toMap(commentRepository.findAll());
    }

    public Map<Long,ResponseTodoCommentDTO> getCommentsByPostId(Long todoId) {
        TodoPost todo = todoPostRepository
                .findById(todoId)
                .orElseThrow(()->new IllegalArgumentException("INVALID_TODO_ID"));

        return commentMapper.toMap(commentRepository.findByTodoPost(todo));
    }

    public Map<Long, ResponseTodoCommentDTO> getCommentById(Long commentId) {
        TodoComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("INVALID_COMMENT_ID"));
        return commentMapper.toMap(comment);
    }

    public Map<Long,ResponseTodoCommentDTO> addComment(RequestTodoCommentDTO commentDTO) {
        TodoComment comment = commentMapper.requestToEntity(commentDTO);
        TodoPost post = todoPostRepository
                .findById(commentDTO.postId())
                .orElseThrow(()->new IllegalArgumentException("TODO_NOT_FOUND"));

        post.addComment(comment);
        todoPostRepository.save(post);
        em.persist(comment);

        List<TodoComment> todoComments = post.getTodoComments();
        return commentMapper.toMap(todoComments.get(todoComments.size() - 1));
    }

    public Map<Long,ResponseTodoCommentDTO> updateComment(RequestUpdateTodoCommentDTO commentDTO) {
        Long id = commentDTO.commentId();

        TodoComment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid comment Id:" + id));
        TodoComment updatedComment = comment
                .toBuilder()
                .content(commentDTO.content())
                .build();

        return commentMapper.toMap(commentRepository.save(updatedComment));
    }

    public void deleteComment(Long id) {
        TodoComment comment = commentRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("COMMENT_NOT_FOUND"));

        TodoPost post = comment.getTodoPost();

        post.removeComment(comment);
        todoPostRepository.save(post);
    }

    public void clear(){
        commentRepository.deleteAll();
    }
}
