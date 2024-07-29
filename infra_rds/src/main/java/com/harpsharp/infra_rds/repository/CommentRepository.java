package com.harpsharp.infra_rds.repository;

import com.harpsharp.infra_rds.entity.Comment;
import com.harpsharp.infra_rds.entity.Post;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Transactional
    List<Comment> findByPost(Post post);
}
