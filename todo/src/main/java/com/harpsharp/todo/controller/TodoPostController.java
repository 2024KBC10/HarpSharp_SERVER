package com.harpsharp.todo.controller;

import com.harpsharp.infra_rds.dto.todo.RequestTodoPostDTO;
import com.harpsharp.infra_rds.dto.todo.RequestUpdateTodoPostDTO;
import com.harpsharp.infra_rds.dto.todo.ResponseTodoPostDTO;
import com.harpsharp.todo.service.TodoPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todo/posts")
public class TodoPostController {

    @Autowired
    private TodoPostService todoPostService;

    @GetMapping
    public List<ResponseTodoPostDTO> getPosts(@RequestParam(required = false) String username) {
        if (username != null) {
            return todoPostService.getTodoPostsByUsername(username);
        } else {
            return todoPostService.getAllTodoPosts();
        }
    }

    @GetMapping("/{id}")
    public ResponseTodoPostDTO getPostById(@PathVariable Long id) {
        return todoPostService.getTodoPostById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + id));
    }

    @PostMapping
    public ResponseTodoPostDTO createPost(@RequestBody RequestTodoPostDTO postDTO) {
        return todoPostService.createTodoPost(postDTO);
    }

    @PutMapping("/{id}")
    public ResponseTodoPostDTO updatePost(@PathVariable Long id, @RequestBody RequestUpdateTodoPostDTO postDTO) {
        return todoPostService.updateTodoPost(id, postDTO);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id) {
        todoPostService.deletePost(id);
    }
}
