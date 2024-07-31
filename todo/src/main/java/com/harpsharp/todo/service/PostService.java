package com.harpsharp.todo.service;

import com.harpsharp.todo.domain.Post;
import com.harpsharp.todo.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public List<Post> getPostsByUsername(String username) {
        return postRepository.findByUsername(username);
    }


    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public Post updatePost(Long id, Post postDetails) {
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + id));
        post.setTitle(postDetails.getTitle());
        post.setContent(postDetails.getContent());
        post.setStatus(postDetails.getStatus());
        post.setStartAt(postDetails.getStartAt());
        post.setEndAt(postDetails.getEndAt());
        post.setLikes(postDetails.getLikes());
        return postRepository.save(post);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}
