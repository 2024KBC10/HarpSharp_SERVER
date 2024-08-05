package com.harpsharp.todo.controller;

import com.harpsharp.infra_rds.entity.TodoPost;
import com.harpsharp.todo.service.TodoPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TodoPostController {

    @Autowired
    private TodoPostService todoPostService;

    @GetMapping("/todo/posts")
    public List<TodoPost> getPosts(@RequestParam(required = false) String username) {
        if (username != null) {
            return todoPostService.getTodoPostsByUsername(username);
        } else {
            return todoPostService.getAllTodoPosts();
        }
    }

    @GetMapping("/{id}")
    public TodoPost getPostById(@PathVariable Long id) {
        return todoPostService.getTodoPostById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + id));
    }

    @PostMapping
    public TodoPost createPost(@RequestBody TodoPost post) {
        return todoPostService.createTodoPost(post);
    }

    @PutMapping("/{id}")
    public TodoPost updatePost(@PathVariable Long id, @RequestBody TodoPost postDetails) {
        return todoPostService.updateTodoPost(id, postDetails);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id) {
        todoPostService.deletePost(id);
    }
}
