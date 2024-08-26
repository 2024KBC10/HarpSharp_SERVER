package com.harpsharp.infra_rds.repository;

import com.harpsharp.infra_rds.entity.board.Comment;
import com.harpsharp.infra_rds.entity.board.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);
    void deleteByPost(Post post);
}
