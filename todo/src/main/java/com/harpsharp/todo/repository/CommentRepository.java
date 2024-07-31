package com.harpsharp.todo.repository;

import com.harpsharp.todo.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
