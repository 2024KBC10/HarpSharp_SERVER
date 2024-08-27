package com.harpsharp.infra_rds.repository;

import com.harpsharp.infra_rds.entity.todo.TodoComment;
import com.harpsharp.infra_rds.entity.todo.TodoPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoCommentRepository extends JpaRepository<TodoComment, Long> {
    List<TodoComment> findByTodoPost(TodoPost todoPost);
    void deleteByTodoPost(TodoPost todoPost);
}
