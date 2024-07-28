package com.example.web_project.controller;

import com.example.web_project.domain.Post;
import com.example.web_project.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/mytask")
    public String getAllPosts(Model model) {
        model.addAttribute("posts", postService.getAllPosts());
        return "posts";
    }

    @GetMapping("/mytask/new")
    public String createPostForm(Model model) {
        model.addAttribute("post", new Post());
        return "create_post";
    }

    @PostMapping("/mytask")
    public String savePost(Post post) {
        postService.savePost(post);
        return "redirect:/mytask";
    }

    @GetMapping("/mytask/{id}")
    public String getPostById(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id).orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + id));
        model.addAttribute("post", post);
        return "post_detail";
    }

    @GetMapping("/mytask/{id}/edit")
    public String editPostForm(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id).orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + id));
        model.addAttribute("post", post);
        return "edit_post";
    }

    @PostMapping("/mytask/{id}/edit")
    public String updatePost(@PathVariable Long id, Post updatedPost) {
        Post post = postService.getPostById(id).orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + id));
        post.setTitle(updatedPost.getTitle());
        post.setContent(updatedPost.getContent());
        postService.savePost(post);
        return "redirect:/mytask/{id}";
    }

    @PostMapping("/mytask/{id}/delete")
    public String deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return "redirect:/mytask";
    }
}
