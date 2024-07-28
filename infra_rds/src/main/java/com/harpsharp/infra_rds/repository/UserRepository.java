package com.harpsharp.infra_rds.repository;

import com.harpsharp.infra_rds.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);
    User findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long userId);
    User findByEmailAndPassword(String email, String password);
    boolean existsByUsername(String username);
}
