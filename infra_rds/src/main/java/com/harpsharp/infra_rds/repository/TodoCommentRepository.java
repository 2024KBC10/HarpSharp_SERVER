package com.harpsharp.infra_rds.repository;

import com.harpsharp.infra_rds.entity.Comment;
import com.harpsharp.infra_rds.entity.Post;
import com.harpsharp.infra_rds.entity.TodoComment;
import com.harpsharp.infra_rds.entity.TodoPost;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface TodoCommentRepository extends JpaRepository<TodoComment, Long> {
    List<TodoComment> findByTodoPost(TodoPost todoPost);
    void deleteByTodoPost(TodoPost todoPost);
}
