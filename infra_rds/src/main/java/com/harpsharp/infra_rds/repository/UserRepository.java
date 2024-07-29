package com.harpsharp.infra_rds.repository;

import com.harpsharp.infra_rds.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Component
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Transactional
    Boolean existsByEmail(String email);
    @Transactional
    User findByEmail(String email);
    @Transactional
    Optional<User> findByUsername(String username);
    @Transactional
    Optional<User> findById(Long userId);
    @Transactional
    User findByEmailAndPassword(String email, String password);
    @Transactional
    boolean existsByUsername(String username);
}
