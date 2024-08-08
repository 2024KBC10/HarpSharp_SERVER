package com.harpsharp.infra_rds.repository;

import com.harpsharp.infra_rds.entity.Post;
import com.harpsharp.infra_rds.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface PostRepository extends JpaRepository<Post, Long> {
    void deleteByUser(User user);
}
