package com.harpsharp.todo.service;

import com.harpsharp.auth.service.UserService;
import com.harpsharp.infra_rds.entity.TodoPost;
import com.harpsharp.infra_rds.entity.User;
import com.harpsharp.infra_rds.repository.TodoPostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TodoPostService {
    private final TodoPostRepository todoPostRepository;
    private final UserService userService;


    public List<TodoPost> getAllTodoPosts() {
        return todoPostRepository.findAll();
    }
    public List<TodoPost> getTodoPostsByUsername(String username) {
        User user = userService.findByUsername(username).orElseThrow(IllegalArgumentException::new);
        return todoPostRepository.findByUser(user);
    }


    public Optional<TodoPost> getTodoPostById(Long id) {
        return todoPostRepository.findById(id);
    }

    public TodoPost createTodoPost(TodoPost TodoPost) {
        return todoPostRepository.save(TodoPost);
    }

    public TodoPost updateTodoPost(Long id, TodoPost todoPostDetails) {
        TodoPost todoPost = todoPostRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid TodoPost Id:" + id));
        todoPost.setTitle(todoPostDetails.getTitle());
        todoPost.setContent(todoPostDetails.getContent());
        todoPost.setStatus(todoPostDetails.getStatus());
        todoPost.setStartAt(todoPostDetails.getStartAt());
        todoPost.setEndAt(todoPostDetails.getEndAt());
        todoPost.setLikes(todoPostDetails.getLikes());
        return todoPostRepository.save(todoPost);
    }

    public void deletePost(Long id) {
        todoPostRepository.deleteById(id);
    }
}
