package com.example.web_project.controller;

import com.example.web_project.domain.Comment;
import com.example.web_project.domain.Post;
import com.example.web_project.service.CommentService;
import com.example.web_project.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

    @PostMapping("/mytask/{postId}/comments")
    public String addComment(@PathVariable Long postId, @RequestParam String content, Model model) {
        Post post = postService.getPostById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + postId));
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setPost(post);
        commentService.saveComment(comment);
        return "redirect:/mytask/" + postId;
    }

    @GetMapping("/mytask/{postId}/comments/{commentId}/edit")
    public String editCommentForm(@PathVariable Long postId, @PathVariable Long commentId, Model model) {
        Comment comment = commentService.getCommentById(commentId).orElseThrow(() -> new IllegalArgumentException("Invalid comment Id:" + commentId));
        model.addAttribute("comment", comment);
        model.addAttribute("postId", postId);
        return "edit_comment";
    }

    @PostMapping("/mytask/{postId}/comments/{commentId}/edit")
    public String updateComment(@PathVariable Long postId, @PathVariable Long commentId, @RequestParam String content, Model model) {
        Comment comment = commentService.getCommentById(commentId).orElseThrow(() -> new IllegalArgumentException("Invalid comment Id:" + commentId));
        comment.setContent(content);
        commentService.saveComment(comment);
        return "redirect:/mytask/" + postId;
    }

    @PostMapping("/mytask/{postId}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable Long postId, @PathVariable Long commentId, Model model) {
        commentService.deleteComment(commentId);
        return "redirect:/mytask/" + postId;
    }
}
