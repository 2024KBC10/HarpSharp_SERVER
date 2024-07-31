package com.harpsharp.board.controller;

import com.harpsharp.board.service.PostService;
import com.harpsharp.infra_rds.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/board/posts")
    public String getAllPosts(Model model) {
        model.addAttribute("posts", postService.getAllPosts());
        return "posts";
    }

    @PostMapping("/board/write")
    public String createPostForm(Model model) {
        model.addAttribute("post", new Post());
        return "create_post";
    }

    @PostMapping("/board/posts")
    public String savePost(Post post) {
        postService.savePost(post);
        return "redirect:/mytask";
    }

    @GetMapping("/board/posts/{id}")
    public String getPostById(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id).orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + id));
        model.addAttribute("post", post);
        return "post_detail";
    }

    @PutMapping("/board/posts/{id}")
    public String editPostForm(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id).orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + id));
        model.addAttribute("post", post);
        return "edit_post";
    }

    @PostMapping("/board/posts/{id}")
    public String updatePost(@PathVariable Long id, Post updatedPost) {
        Post post = postService.getPostById(id).orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + id));
        post.setTitle(updatedPost.getTitle());
        post.setContent(updatedPost.getContent());
        postService.savePost(post);
        return "redirect:/board/{id}";
    }

    @DeleteMapping("/board/posts/{id}")
    public String deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return "redirect:/board";
    }
}
