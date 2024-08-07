package com.harpsharp.infra_rds.repository;

import com.harpsharp.infra_rds.entity.TodoPost;
import com.harpsharp.infra_rds.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@Transactional
public interface TodoPostRepository extends JpaRepository<TodoPost, Long> {
    @Query("SELECT p FROM TodoPost p JOIN FETCH p.user")
    List<TodoPost> findByUser(User user);
}