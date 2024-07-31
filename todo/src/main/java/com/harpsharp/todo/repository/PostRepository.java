package com.harpsharp.todo.repository;

import com.harpsharp.todo.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUsername(String username);
}