package com.harpsharp.infra_rds.repository;

import com.harpsharp.infra_rds.entity.board.Post;
import com.harpsharp.infra_rds.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    void deleteByUser(User user);
}
