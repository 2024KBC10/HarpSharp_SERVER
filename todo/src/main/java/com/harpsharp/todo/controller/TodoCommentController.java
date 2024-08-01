package com.harpsharp.todo.controller;

import com.harpsharp.infra_rds.entity.TodoComment;
import com.harpsharp.todo.service.TodoCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todo/comments")
public class TodoCommentController {

    @Autowired
    private TodoCommentService todoCommentService;

    @GetMapping
    public List<TodoComment> getAllComments() {
        return todoCommentService.getAllComments();
    }

    @GetMapping("/todo/{id}")
    public TodoComment getCommentById(@PathVariable Long id) {
        return todoCommentService.getCommentById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid comment Id:" + id));
    }

    @PostMapping("/todo")
    public TodoComment createComment(@RequestBody TodoComment comment) {
        return todoCommentService.createComment(comment);
    }

    @PutMapping("/todo/{id}")
    public TodoComment updateComment(@PathVariable Long id, @RequestBody TodoComment commentDetails) {
        return todoCommentService.updateComment(id, commentDetails);
    }

    @DeleteMapping("/todo/{id}")
    public void deleteComment(@PathVariable Long id) {
        todoCommentService.deleteComment(id);
    }
}
