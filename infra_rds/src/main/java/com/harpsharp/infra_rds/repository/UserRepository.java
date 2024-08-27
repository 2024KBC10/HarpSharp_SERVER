package com.harpsharp.infra_rds.repository;

import com.harpsharp.infra_rds.entity.album.ProfileImage;
import com.harpsharp.infra_rds.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long userId);
    Optional<User> findByEmailAndPassword(String email, String password);
    Optional<User> findByProfileImage(ProfileImage profileImage);
    boolean existsByUsername(String username);
}
