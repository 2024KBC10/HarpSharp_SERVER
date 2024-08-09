package com.harpsharp.todo.controller;

import com.harpsharp.infra_rds.dto.todo.RequestTodoCommentDTO;
import com.harpsharp.infra_rds.dto.todo.ResponseTodoCommentDTO;
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
    public List<ResponseTodoCommentDTO> getAllComments() {
        return todoCommentService.getAllComments();
    }

    @GetMapping("/{id}")
    public ResponseTodoCommentDTO getCommentById(@PathVariable Long id) {
        return todoCommentService.getCommentById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid comment Id:" + id));
    }

    @PostMapping
    public ResponseTodoCommentDTO createComment(@RequestBody RequestTodoCommentDTO commentDTO) {
        return todoCommentService.createComment(commentDTO);
    }

    @PutMapping("/{id}")
    public ResponseTodoCommentDTO updateComment(@PathVariable Long id, @RequestBody RequestTodoCommentDTO commentDTO) {
        return todoCommentService.updateComment(id, commentDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable Long id) {
        todoCommentService.deleteComment(id);
    }
}
